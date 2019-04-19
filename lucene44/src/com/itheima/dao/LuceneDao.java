package com.itheima.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.itheima.bean.Article;
import com.itheima.utils.ArticleUtils;
import com.itheima.utils.LuceneUtils;

/**
 * 
 * 使用lucene 来操作索引库...
 * 
 * @author Administrator
 *
 */
public class LuceneDao {
	/**
	 * 增删改索引都是通过indexWriter 对象来完成...
	 * @throws IOException 
	 * 
	 * 
	 */
	public void addIndex(Article article) throws IOException{
		IndexWriter indexWriter=LuceneUtils.getIndexWriter();
		Document doc=ArticleUtils.articleToDocument(article);
		indexWriter.addDocument(doc);
		indexWriter.close();
	}
	/**
	 * 
	 * 删除索引，根据字段对应的值进行删除...
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @throws IOException
	 */
	public void delIndex(String fieldName,String fieldValue) throws IOException{
		IndexWriter indexWriter=LuceneUtils.getIndexWriter();
		
		
		
		Term term=new Term(fieldName, fieldValue);
		//delete from table where condition
		indexWriter.deleteDocuments(term);
		
		indexWriter.close();
		
	}
	
	
	/**
	 * 
	 * 先删除符合条件的记录，再创建一个符合条件的记录....
	 * @param fieldName
	 * @param fieldValue
	 * @param article
	 * @throws IOException
	 */
	public void updateIndex(String fieldName,String fieldValue,Article article) throws IOException{
		IndexWriter indexWriter=LuceneUtils.getIndexWriter();
		Term term=new Term(fieldName, fieldValue);
		Document doc=ArticleUtils.articleToDocument(article);
		/**
		 * 
		 * 1:设置更新的条件
		 * 
		 * 
		 * 2：设置更新的内容的对象...
		 * 
		 * 
		 */
		indexWriter.updateDocument(term, doc);
		
		indexWriter.close();
	}
	/**
	 * 
	 *   显示第一页 的数据  0,10
	 *  
	 *   显示第二页 的数据  10,10
	 *   
	 *    显示第三页 的数据  20,10
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	public List<Article> findIndex(String keywords,int start,int rows) throws Exception{
		IndexSearcher indexSearcher=LuceneUtils.getIndexSearcher();
		//需要根据那几个字段进行检索...
		String   fields []={"title","content"};
		
		//title:yi  title：抑,title:症
		//第一种类型的条件..
//		Query query=new TermQuery(new Term("author","毕加索"));
		//第二种类型的条件
		QueryParser queryParser=new MultiFieldQueryParser(LuceneUtils.getMatchVersion(),fields,LuceneUtils.getAnalyzer());
		//不同的规则构造不同的子类..
		//title:keywords  ，content:keywords
		Query query=queryParser.parse(keywords);
		
		//这里检索的是索引目录
		
		//会把整个索引目录都读取一边...
		//检索符合query 前面N条记录...
		TopDocs topDocs=indexSearcher.search(query, start+rows);
		System.out.println("总记录数==total=="+topDocs.totalHits);
		ScoreDoc scoreDocs []=topDocs.scoreDocs;
		Article article=null;
		List<Article> articlelist=new ArrayList<Article>();
		
		
		//scoreDocs.length  vs（比较）   start+rows  取小值
		//在java jdk 里面提供了一个类，可以用来比较两个数字类型的值，取小值..
		int endResult=Math.min(scoreDocs.length, start+rows);
		
		for(int i=start;i<endResult;i++){
			//docID lucene 的索引库里面有很多的document，lucene 为每个document 定义一个编号，唯一标识.. 自增长
			int docID=scoreDocs[i].doc;
			System.out.println("编号的标识==="+docID);
			article=new Article();
			Document document=indexSearcher.doc(docID);
			article.setId(Integer.parseInt(document.get("id")));
			article.setTitle(document.get("title"));
			article.setContent(document.get("content"));
			article.setLink(document.get("link"));
			article.setAuthor(document.get("author"));
			articlelist.add(article);
		}
		return articlelist;
	}
}
