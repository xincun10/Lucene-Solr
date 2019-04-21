package com.itheima.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * SolrJ管理
 * 增删改查
 * @author Administrator
 *
 */
public class SolrJManager {

	//添加索引
	@Test
	public void testAdd() throws Exception
	{
		String baseURL = "http://192.168.42.128:8080/solr/";
		//如果需要指定核，可以修改URL为"http://192.168.42.128:8080/solr/collection2"
		//单机版
		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		SolrInputDocument doc = new SolrInputDocument();
		//id是必须的
		doc.setField("id", "suibian");
		doc.setField("name", "yesuibian");
		
		//添加
		solrServer.add(doc);
		//操作完成后手动提交
		solrServer.commit();
		
	}
	
	//删除操作，分为条件删除和全部删除
	@Test
	public void testDeleteAll() throws Exception
	{
		String baseURL = "http://192.168.42.128:8080/solr/";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		//删除全部
//		solrServer.deleteByQuery("*:*", 1000);//设置1000ms自动提交
		//部分删除
		solrServer.deleteByQuery("name:yesuibian", 1000);
	}
	
	//更新操作
	//更新操作和添加一致solrServer.add(doc);，如果id不同就是添加操作，如果id相同就是更新操作。
	
	//查询操作
	@Test
	public void testSearch() throws Exception
	{
		String baseURL = "http://192.168.42.128:8080/solr/";
		SolrServer solrServer = new HttpSolrServer(baseURL);
		
		//查询条件
		SolrQuery solrQuery = new SolrQuery();
		//关键词
//		solrQuery.set("q", "*:*");//查询所有，如果不设置分页，默认只返回第一页
		solrQuery.setQuery("name:yesuibian");
		//过滤条件
		solrQuery.set("fq", "catelog:yesuibian");
		//价格区间
		solrQuery.set("fq", "price:[* TO 10]");
		//价格排序
		solrQuery.addSort("price", ORDER.desc);
		//分页
		solrQuery.setStart(0);//Specifies the number of rows to skip
		solrQuery.setRows(5);//每页5条数据
		//默认域
		solrQuery.set("df", "name");
		//只查询指定域
		solrQuery.set("fl", "id,name");
		//高亮 
		solrQuery.setHighlight(true);//打开高亮开关
		solrQuery.addHighlightField("name");//设置高亮域
		solrQuery.setHighlightSimplePre("<span style='color:blue'>");//设置高亮前缀标签
		solrQuery.setHighlightSimplePost("</span>");//设置高亮后缀标签
		
		//执行查询
		QueryResponse response = solrServer.query(solrQuery);
		//文档结果集
		SolrDocumentList docs = response.getResults();
		//获取高亮
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		long numFound = docs.getNumFound();
		System.out.println(numFound);
		for(SolrDocument doc:docs)
		{
			System.out.println(doc.get("name"));
			System.out.println(doc.get("id"));
			//高亮显示
			Map<String, List<String>> map = highlighting.get(doc.get("id"));
			List<String> list = map.get("name");//高亮域
			System.out.println(list.get(0));
		}
	}
}
