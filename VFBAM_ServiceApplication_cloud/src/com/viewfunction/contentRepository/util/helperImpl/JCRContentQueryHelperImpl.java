package com.viewfunction.contentRepository.util.helperImpl;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import com.viewfunction.contentRepository.contentBureau.BaseContentObject;
import com.viewfunction.contentRepository.contentBureauImpl.JCRContentObjectImpl;
import com.viewfunction.contentRepository.util.exception.ContentReposityDataException;
import com.viewfunction.contentRepository.util.exception.ContentReposityException;
import com.viewfunction.contentRepository.util.factory.ContentComponentFactory;
import com.viewfunction.contentRepository.util.helper.BinaryContent;
import com.viewfunction.contentRepository.util.helper.ContentQueryHelper;
import com.viewfunction.contentRepository.util.helper.TextContent;

public class JCRContentQueryHelperImpl implements ContentQueryHelper{

	@Override
	public List<BinaryContent> selectBinaryContentsByFullTextSearch(BaseContentObject contentObject, String contentValue) throws ContentReposityException {		
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {			
			String np=jmpl.getJcrNode().getPath();			
			String sql = "SELECT * FROM [vfcr:resource] AS file WHERE ISDESCENDANTNODE(['"+np+"']) and contains(file.*,'"+contentValue.trim()+"')";
			return selectBinaryContentsBySQL2(contentObject,sql);			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}

	@Override
	public List<TextContent> selectTextContentsByEncoding(BaseContentObject contentObject, String encodingValue)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {			
			String np=jmpl.getJcrNode().getPath();
			String sql = "SELECT * FROM [vfcr:resource] where ISDESCENDANTNODE(['"+np+"']) and [jcr:encoding] LIKE '%"+encodingValue.trim()+"%'";	
			return selectTextContentsBySQL2(contentObject,sql);					
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
	
	@Override
	public List<BinaryContent> selectBinaryContentsByMimeType(BaseContentObject contentObject, String mimeTypeValue)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {			
			String np=jmpl.getJcrNode().getPath();			
			String sql = "SELECT * FROM [vfcr:resource] where ISDESCENDANTNODE(['"+np+"']) and [jcr:mimeType] LIKE '%"+mimeTypeValue.trim()+"%'";	
			return selectBinaryContentsBySQL2(contentObject,sql);			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			e.printStackTrace();
			throw cpe;
		}
	}
	
	@Override
	public List<TextContent> selectTextContentsByMimeType(BaseContentObject contentObject, String mimeTypeValue)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {			
			String np=jmpl.getJcrNode().getPath();				
			String sql = "SELECT * FROM [vfcr:resource] where ISDESCENDANTNODE(['"+np+"']) and [jcr:mimeType] LIKE '%"+mimeTypeValue.trim()+"%' and [jcr:encoding] <>''";	
			return selectTextContentsBySQL2(contentObject,sql);			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}
	
	@Override
	public List<BinaryContent> selectBinaryContentsByTitle(BaseContentObject contentObject, String titleValue)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {			
			String np=jmpl.getJcrNode().getPath();
			String sql = "SELECT * FROM [vfcr:resource] where ISDESCENDANTNODE(['"+np+"']) and [vfcr:contentName] LIKE '%"+titleValue.trim()+"%'";	
			System.out.println(np);
			System.out.println(sql);
			
			return selectBinaryContentsBySQL2(contentObject,sql);			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	} 
	
	@Override
	public List<TextContent> selectTextContentsByTitle(BaseContentObject contentObject, String titleValue)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {			
			String np=jmpl.getJcrNode().getPath();			
			String sql = "SELECT * FROM [vfcr:resource] where ISDESCENDANTNODE(['"+np+"']) and [vfcr:contentName] LIKE '%"+titleValue.trim()+"%' and [jcr:encoding] <>''";	
			return selectTextContentsBySQL2(contentObject,sql);			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	} 
	
	private List<BinaryContent> selectBinaryContentsBySQL2(BaseContentObject contentObject, String sql2Str)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {
			QueryManager qm = jmpl.getJcrSession().getWorkspace().getQueryManager();
			Query q = qm.createQuery(sql2Str, Query.JCR_SQL2);
			QueryResult result = q.execute();			
			NodeIterator nodeIterator=result.getNodes();
			List<BinaryContent> bcl=new ArrayList<BinaryContent>();			
			while(nodeIterator.hasNext()){				
				Node n=nodeIterator.nextNode();							
				JCRBinaryContentImpl jcrBco= (JCRBinaryContentImpl)ContentComponentFactory.createBinaryContentObject();
				jcrBco.setBinaryContainerNode(n.getParent());
				jcrBco.setContentBinary(n.getProperty("jcr:data").getBinary());
				jcrBco.setMimeType(n.getProperty("jcr:mimeType").getString()); 
				jcrBco.setContentName(n.getProperty("vfcr:contentName").getString());           		
           		jcrBco.setContentDescription(n.getProperty("vfcr:contentDescription").getString());  
           		if(n.hasProperty("vfcr:createDate")){
           			jcrBco.setCreated(n.getProperty("vfcr:createDate").getDate());               			
           		}
           		if(n.hasProperty("vfcr:creator")){
           			jcrBco.setCreatedBy(n.getProperty("vfcr:creator").getString());               			
           		}               		
           		if(n.hasProperty("vfcr:lastUpdatePerson")){
           			jcrBco.setLastModifiedBy(n.getProperty("vfcr:lastUpdatePerson").getString()); 
           		}   
           		if(n.hasProperty("vfcr:lastUpdateDate")){
           			jcrBco.setLastModified(n.getProperty("vfcr:lastUpdateDate").getDate()); 
           		}
           		bcl.add(jcrBco);							
			}
			return bcl;			
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}
	}	

	private List<TextContent> selectTextContentsBySQL2(BaseContentObject contentObject, String sql2Str)throws ContentReposityException {
		JCRContentObjectImpl jmpl=(JCRContentObjectImpl)contentObject;
		try {								
			QueryManager qm = jmpl.getJcrSession().getWorkspace().getQueryManager();
			Query q = qm.createQuery(sql2Str, Query.JCR_SQL2);
			QueryResult result = q.execute();			
			NodeIterator nodeIterator=result.getNodes();
			List<TextContent> bcl=new ArrayList<TextContent>();			
			while(nodeIterator.hasNext()){				
				Node n=nodeIterator.nextNode();	       	
        		JCRTextContentImpl jcrBco= (JCRTextContentImpl)ContentComponentFactory.createTextContentObject(); 
        		jcrBco.setBinaryContainerNode(n.getParent());
        		jcrBco.setEncoding(n.getProperty("jcr:encoding").getString());
        		jcrBco.setContentBinary(n.getProperty("jcr:data").getBinary());
				jcrBco.setMimeType(n.getProperty("jcr:mimeType").getString()); 
				jcrBco.setContentName(n.getProperty("vfcr:contentName").getString());           		
           		jcrBco.setContentDescription(n.getProperty("vfcr:contentDescription").getString()); 
           		if(n.hasProperty("vfcr:createDate")){
           			jcrBco.setCreated(n.getProperty("vfcr:createDate").getDate());               			
           		}
           		if(n.hasProperty("vfcr:creator")){
           			jcrBco.setCreatedBy(n.getProperty("vfcr:creator").getString());               			
           		}               		
           		if(n.hasProperty("vfcr:lastUpdatePerson")){
           			jcrBco.setLastModifiedBy(n.getProperty("vfcr:lastUpdatePerson").getString()); 
           		}   
           		if(n.hasProperty("vfcr:lastUpdateDate")){
           			jcrBco.setLastModified(n.getProperty("vfcr:lastUpdateDate").getDate()); 
           		} 
            	bcl.add(jcrBco);	
			}
			return bcl;					
		} catch (RepositoryException e) {
			ContentReposityDataException cpe=new ContentReposityDataException();
			cpe.initCause(e);
			throw cpe;
		}		
	}
}
