package com.itheima.lucene;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 索引维护
 * 添加
 * 删除
 * 修改
 * 查询
 * @author Administrator
 *
 */
public class LuceneManager {
	
	public IndexWriter getIndexWriter() throws Exception
	{
		Directory directory = FSDirectory.open(new File("H:\\lucene\\temp\\index"));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44, analyzer);
		return new IndexWriter(directory, config);
	}
	
	//全删除
	@Test
	public void testDelAll() throws Exception
	{
		IndexWriter indexWriter = getIndexWriter();
		indexWriter.deleteAll();
		indexWriter.close();
	}

	//添加索引
	@Test
	public void addIndex() throws Exception {
		// 第一步：创建一个java工程，并导入jar包。
		// 第二步：创建一个indexwriter对象。
		// 1）指定索引库的存放位置Directory对象
		// 2）指定一个分析器，对文档内容进行分析。
		Directory directory = FSDirectory.open(new File("H:\\lucene\\temp\\index"));
		Version matchVersion = Version.LUCENE_44;// 当前Lucene版本
		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(matchVersion, analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		File f = new File("H:\\lucene\\searchsource");// 原始文档存放目录
		File[] listFiles = f.listFiles();
		for (File file : listFiles) {
			// 第二步：创建document对象。
			Document document = new Document();
			// 第三步：创建field对象，将field添加到document对象中。
			// 文件名称
			String file_name = file.getName();
			// 域Field
			Field fileNameField = new TextField("fileName", file_name, Store.YES);
			// 文件大小
			long file_size = FileUtils.sizeOf(file);
			Field fileSizeField = new LongField("fileSize", file_size, Store.YES);
			// 文件路径
			String file_path = file.getPath();
			Field filePathField = new StoredField("filePath", file_path);
			// 文件内容
			String file_content = FileUtils.readFileToString(file);
			Field fileContentField = new TextField("fileContent", file_content, Store.YES);

			document.add(fileNameField);
			document.add(fileSizeField);
			document.add(filePathField);
			document.add(fileContentField);
			// 第四步：使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
			indexWriter.addDocument(document);
		}

		// 第五步：关闭IndexWriter对象。
		indexWriter.close();

	}
	
	//根据条件删除
	@Test
	public void testDelete() throws Exception
	{
		IndexWriter indexWriter = getIndexWriter();
		Query query = new TermQuery(new Term("fileName", "one"));
		indexWriter.deleteDocuments(query);
		indexWriter.close();
	}
	
	//修改
	public void testUpdate() throws Exception
	{
		IndexWriter indexWriter = getIndexWriter();
		Document document = new Document();
		document.add(new TextField("fileN", "测试文件名", Store.YES));
		document.add(new TextField("fileC", "测试文件内容", Store.YES));
		indexWriter.updateDocument(new Term("fileName", "lucene"), document);
		indexWriter.close();
	}
}
