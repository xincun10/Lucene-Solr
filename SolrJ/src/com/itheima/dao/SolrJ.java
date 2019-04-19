package com.itheima.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.junit.Test;

import com.itheima.bean.Article;

/***
 * 
 * 
 * 使用solrj 来调用solr 的服务...
 * @author Administrator
 *
 */
public class SolrJ {
	
	
	@Test
	public void addIndex() throws SolrServerException, IOException{
		
		
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);
		
//		List<Article> list=new ArrayList<Article>();
//		Article article=null;
//		for(int i=1;i<=25;i++){
//			article=new Article();
//			//第二种添加方式...
//			article.setId(i);
//			article.setTitle("高富帅");
//			article.setContent("白富美");
//			article.setPrice(19);
//			article.setName("张浩亮");
//			list.add(article);
//		}
		
		
		
		
		//第一种添加方式...
		SolrInputDocument document=new SolrInputDocument();
		
		document.addField("id", "9527");
		
		document.addField("name", "武书静");
		
		document.addField("xxxxx_ss", "很有文艺范的一个名字");
		
	
		
		
		solr.add(document);
		
//		solr.addBeans(list);
		
	//	solr.addBean(article);
		
		solr.commit();
	}

	@Test
	public void Del() throws SolrServerException, IOException{
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);
		
		solr.deleteById("999");
		
		solr.commit();
		
	}
	//更新的话如果是id 相同，它会直接更新 
	
	@Test
	public void testFind() throws SolrServerException{
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);
		
		//以后参数都是通过这个对象去构造...
		SolrQuery solrParams=new SolrQuery();
		
		solrParams.setQuery("description:小键");  
		
		//分页
//		solrParams.setStart(0);
//		
//		solrParams.setRows(10);
		
		//开启高亮...
		solrParams.setHighlight(true);
		
		//高亮显示的格式...
		solrParams.setHighlightSimplePre("<font color='red'>");
		solrParams.setHighlightSimplePost("</font>");
		
		
		
		//我需要那几个字段进行高亮...
		
		solrParams.setParam("hl.fl", "description");
		QueryResponse queryResponse=solr.query(solrParams);
		
		//返回所有的结果...
		SolrDocumentList documentList=queryResponse.getResults();
		
		Map<String, Map<String, List<String>>> maplist=queryResponse.getHighlighting();
		
		//返回高亮之后的结果..
		
		for(SolrDocument solrDocument:documentList){
			Object id=solrDocument.get("id");
//			Object name=solrDocument.get("name");
//			Object content=solrDocument.get("description");
//			System.out.println(id);
//			System.out.println(name);
//			System.out.println(content);
			Map<String, List<String>>  fieldMap=maplist.get(id);
			List<String> stringlist=fieldMap.get("description");
			
			System.out.println(stringlist);
			
		}
		
	}
	
}
