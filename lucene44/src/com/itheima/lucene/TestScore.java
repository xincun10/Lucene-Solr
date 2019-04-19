package com.itheima.lucene;


import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import sun.applet.Main;

import com.itheima.utils.LuceneUtils;

public class TestScore {

	public static void main(String[] args) throws Exception {
		//内容一样，搜索关键字一样，得分也是一样的..
		//我们可以人工的去干预这个得分...
		//得分跟搜索关键字在文章当中出现的频率，次数，位置有关系...
		testSearcher("抑郁症");
		
		/**
		 * 
		 * seo:
		 * 
		 * 百度会把那些数据获取过去建立索引...
		 * 
		 * 
		 */
	}
	
		
	
	public static void testSearcher(String keywords) throws Exception{
		IndexSearcher indexSearcher=LuceneUtils.getIndexSearcher();
		//需要根据那几个字段进行检索...
		String   fields []={"title"};
		
		QueryParser queryParser=new MultiFieldQueryParser(LuceneUtils.getMatchVersion(),fields,LuceneUtils.getAnalyzer());
		Query query=queryParser.parse(keywords);
		
		//检索符合query 前面N条记录...
		TopDocs topDocs=indexSearcher.search(query, 100);
		ScoreDoc scoreDocs []=topDocs.scoreDocs;
		
		for(ScoreDoc scoreDoc:scoreDocs){
			//VSM
			Document document=indexSearcher.doc(scoreDoc.doc);
			
			System.out.println("文档编号，id"+document.get("id")	+"====得分===="+scoreDoc.score);
			
		}
		
		
		
	}
	
	
}
