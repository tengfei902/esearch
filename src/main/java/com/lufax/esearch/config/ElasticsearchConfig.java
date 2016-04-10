package com.lufax.esearch.config;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.lufax.esearch.annotations.Document;
import com.lufax.esearch.client.ElasticsearchClientFactory;
import com.lufax.esearch.config.xml.ESearchConfigReader;
import com.lufax.esearch.config.xml.IndexConfig;
import com.lufax.esearch.config.xml.SearchConfig;
import com.lufax.esearch.config.xml.TypeConfig;
import com.lufax.esearch.core.SearchHelper;
import com.lufax.esearch.domain.BaseSearchDomain;
import com.lufax.esearch.exception.ESearchException;
import com.lufax.esearch.reader.ISearchReader;
import com.lufax.esearch.util.BeanInfoUtil;

public class ElasticsearchConfig {
	private ElasticsearchClientFactory clientFactory;
	private SearchHelper searchHelper;
	private String configPath = String.format("service%ssearch-config%s",File.separator);
	
	public ElasticsearchClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setClientFactory(ElasticsearchClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public ElasticsearchConfig() {
		if(null == clientFactory) {
			throw new ESearchException("Client factory not initialized");
		}
		searchHelper = new SearchHelper(clientFactory);
		initConfig();
	}
	
	private void initConfig() {
		File configDir = new File(configPath);
		if(!configDir.exists()) {
			throw new ESearchException(String.format("Config path: %s not exist",configPath));
		}
		
		if(!configDir.isDirectory()) {
			throw new ESearchException(String.format("%s not an available directory", configPath));
		}
		
		File[] configFiles = configDir.listFiles();
		if(null == configFiles || configFiles.length==0) {
			throw new ESearchException(String.format("No config files exist in config path:%s",configPath));
		}
		
		for(File configFile:configFiles) {
			Reader reader;
			try {
				reader = new FileReader(configFile);
				SearchConfig searchConfig = ESearchConfigReader.parseConfig(reader, SearchConfig.class);
				registerConfig(searchConfig);
			} catch (Exception e) {
				throw new ESearchException(e.getMessage());
			}
		}
	}
	
	private void registerConfig(SearchConfig searchConfig) throws Exception {
		List<IndexConfig> indexConfigs = searchConfig.getIndexConfigs();
		for(IndexConfig indexConfig:indexConfigs) {
			for(TypeConfig typeConfig: indexConfig.getTypeConfigs()) {
				SearchEntity searchEntity = new SearchEntity();
				searchEntity.setIndex(indexConfig.getIndex());
				searchEntity.setType(typeConfig.getType());
				searchEntity.setIndexStoreType(indexConfig.getIndexStoreType());
				searchEntity.setRefreshInterval(indexConfig.getRefreshInterval());
				searchEntity.setReplicas(indexConfig.getReplicas());
				searchEntity.setShards(indexConfig.getShards());
				String readerPath = typeConfig.getReader().getClazz();
				ISearchReader reader = (ISearchReader)Class.forName(readerPath).newInstance();
				searchEntity.setReader(reader);
				String domainPath = typeConfig.getDomain().getClazz();
				Class domainClass = Class.forName(domainPath);
				if(!BaseSearchDomain.class.isAssignableFrom(domainClass)) {
					throw new ESearchException(String.format("Class %s must extends from SearchBaseDomain", domainPath));
				}
				if(!domainClass.isAnnotationPresent(Document.class)) {
					throw new ESearchException(String.format("Class %s must present annation @Document",domainPath));
				}
				searchEntity.setDomainClass(domainClass);
				Map<String, PropertyDescriptor> propertyMap = BeanInfoUtil.parseBeanInfo(domainClass);
				searchEntity.setPropertyMap(propertyMap);
				try {
					SearchEntityRegister.register(domainClass, searchEntity);
				} catch(Exception e) {
					throw new ESearchException(String.format("Identical Config Error for @Document class %s",domainPath));
				}
			}
		}
	}
	
	public SearchHelper searchHelper() {
		return this.searchHelper;
	}
}
