package com.itheima.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import com.itheima.bean.Article;
import com.itheima.utils.LuceneUtils;

/**
 * 
 * 
 * 对查询出来的结果所包含的搜索关键字进行高亮...
 * @author Administrator
 *
 */
public class TestHighLighter {

	
	public static void main(String[] args) throws Exception {
		String fields []={"title"};
		
		
		/**
		 * 使用lucene 自带的高亮器进行高亮...
		 */
		String keywords="lucene";
		
		QueryParser queryParser=new MultiFieldQueryParser(LuceneUtils.getMatchVersion(),fields,LuceneUtils.getAnalyzer());
		Query query=queryParser.parse(keywords);
		IndexSearcher indexSearcher=LuceneUtils.getIndexSearcher();
		TopDocs topDocs=indexSearcher.search(query,100);
		
		/**
		 * 使用lucene 自带的高亮起进行高亮..
		 * 
		 */
		
//		solr 是基于lucene 的一个全文检索服务器.
		
		
		//solr 是基于<font  color='red' >lucene</font> 的一个全文检索服务器.
		//高亮显示的格式...
		Formatter formatter=new SimpleHTMLFormatter("<font color='red'>", "</font>");
		
		
		//query 里面条件，条件里面有搜索关键字
		Scorer fragmentScorer=new QueryScorer(query); 
		
		//构造高亮气...
		/**
		 * 1:我要高亮成什么颜色
		 * 2：我要将那些关键字进行高亮...
		 * 
		 */
		Highlighter highlighter=new Highlighter(formatter, fragmentScorer);
		
		Article article=null;
		
		System.out.println("总记录数==="+topDocs.totalHits);
		for(ScoreDoc scoreDoc:topDocs.scoreDocs){
			
			article=new Article();
			Document document=indexSearcher.doc(scoreDoc.doc);
			
			String title=document.get("title");
			String content=document.get("content");
			
			
			
			System.out.println("没有高亮之前的结果title=="+title);
			System.out.println("没有高亮之前的结果content=="+content);
			
			//将某段文本高亮，返回高亮过后的结果...
			String hightitle=highlighter.getBestFragment(LuceneUtils.getAnalyzer(), "title", title);
			//如果这个字段当中没有包含搜索关键字，你对这个字段的值进行高亮，返回的是null...
			String highcontent=highlighter.getBestFragment(LuceneUtils.getAnalyzer(), "content", content);
			System.out.println("高亮之后的结果------------------------------------------------------------");
			System.out.println("高亮之后的结果hightitle=="+hightitle);
			System.out.println("高亮之后的结果highcontent=="+highcontent);
			if(hightitle==null){
				article.setTitle(title);
			}else{
				article.setTitle(hightitle);
			}
			
			if(highcontent==null){
				
				article.setContent(content);
			}else{
				
				article.setContent(highcontent);
			}
			
			//最终用户得到结果...
			System.out.println("---"+article.getTitle());
			
			System.out.println("---"+article.getContent());
			
		}
		
		
	}
}
