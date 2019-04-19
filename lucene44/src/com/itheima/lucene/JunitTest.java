package com.itheima.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.Term;
import org.junit.Test;

import com.itheima.bean.Article;
import com.itheima.dao.LuceneDao;

public class JunitTest {

	
	private LuceneDao luceneDao=new LuceneDao();
	
	@Test
	public void addIndex() throws IOException{
		for(int i=33;i<=33;i++){
			Article article=new Article();
			article.setId(i);
			article.setTitle("solr 是基于lucene 的一个全文检索服务器.");
			article.setContent("有人问毕加索：你的画我看不懂，毕加索：鸟叫声好听，好听，听得懂吗，不懂");
			article.setAuthor("爱新觉罗杜小文");
			article.setLink("www.itheima.com");
			luceneDao.addIndex(article);
		}
	
	}
	
	@Test
	public void testDel() throws IOException{
		luceneDao.delIndex("title", "抑");
		
	}
	
	
	@Test
	public void testUpdate() throws IOException{
		
	   String fieldName="title";
	   
	   String fieldValue="抑";
	   
	   Article article=new Article();
	   
	   
	   article.setId(9527);
	   
	   article.setAuthor("王昭厅");
	   
	   
	   article.setTitle("你厅歌很黄");
	   
	   article.setContent("你厅歌今天用的是黑妹洗发水");
	   
	   article.setLink("http://www.itheima.com");
	   
		
	   luceneDao.updateIndex(fieldName, fieldValue, article);
		
	}
	
	
	@Test
	public void testsearcher() throws Exception{
		String keywords="抑郁症";
		//title content   textfield 现在使用的分词器是单字分词..
		//title:有
		//title:
		//author:毕加索
		List<Article> listArticles=luceneDao.findIndex(keywords,0,10);
		for(Article article:listArticles){
			System.out.println(article.getId());
			System.out.println(article.getAuthor());
			System.out.println(article.getLink());
			System.out.println(article.getContent());
			System.out.println(article.getTitle());
			
		}
	}
}
