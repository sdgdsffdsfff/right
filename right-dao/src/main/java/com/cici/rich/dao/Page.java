package com.cici.rich.dao;

/**
 * @ClassName: Page
 * @Description: 分页
 * @author qianj
 * @date 2014年9月9日 下午3:45:12
 *
 */
public class Page
{
    // 页号
    private int page = 1;

    // 记录总数
    private int counts = -1;

    // 每页显示记录数
    private int perPageSize = 20;
    
    // 总页数
    private int totalPage = 1;

    // 当前页
    private int currentPage = 1;

    // 开始记录数
    private int firstResult = 1;

    private String sortName;
    
    private String sortOrder="desc";

    public String getSortName(){
		return sortName;
	}

	public void setSortName(String sortName){
		this.sortName = sortName;
	}

	public String getSortOrder(){
		return sortOrder;
	}

	public void setSortOrder(String sortOrder){
		this.sortOrder = sortOrder;
	}

	/**
     * 
     *
     */
    public Page(){
    }

    /**
     * @param page
     * @param perPageSize
     */
    public Page(int page){
        this.page = page;
       
    }
    /**
     * @param page
     * @param perPageSize
     */
    public Page(int page, int perPageSize){
        this.page = page;
        this.perPageSize = perPageSize;
       
    }

    /**
     * @param page
     * @param counts
     * @param perPageSize
     */
    public Page(int page, int counts, int perPageSize){
        this.page = page;
        this.counts = counts;
        this.perPageSize = perPageSize;
       
    }

    /**
     * @param page
     * @param counts
     * @param perPageSize
     * @param sortName
     */
    public Page(int page, int counts, int perPageSize, String sortName){
        this.page = page;
        this.counts = counts;
        this.perPageSize = perPageSize;
        this.sortName=sortName;
       
    }
    /**
     * @param page
     * @param counts
     * @param perPageSize
     * @param sortName
     */
    public Page(int page, int counts, int perPageSize, String sortName,String sortOrder){
        this.page = page;
        this.counts = counts;
        this.perPageSize = perPageSize;
        this.sortName=sortName;
        this.sortOrder=sortOrder;
    }
   

    public int getPage(){
        return this.page;
    }

    public void setPage(int aPage){
        this.page = aPage;
    }

    public int getCounts(){
        return this.counts;
    }

    public void setCounts(int counts){
        this.counts = counts;

        if (this.counts >= 0)
        {
            // 根据记录总数和每页显示数和当前页，
            // 设置第一条记录，并得到相应链接等等
            this.doPageBreak();
        }
    }

    public int getPerPageSize(){
        return this.perPageSize;
    }

    public void setPerPageSize(int perPageSize){
        this.perPageSize = perPageSize;
    }

    public int getFirstResult(){
        return this.firstResult;
    }

    /**
     * 进行分页处理
     */
    public void doPageBreak(){
        // 计算所有的页面数
        this.totalPage = (int)Math.ceil((this.counts + this.perPageSize - 1) / this.perPageSize);

        int intPage = this.page<=1?1:this.page;
        
        if (intPage > this.totalPage)
        {
            this.currentPage = this.totalPage;
        }
        else
        {
            this.currentPage = intPage;
        }

        // 得到当前页面第一条记录
        this.firstResult = (this.currentPage - 1) * this.perPageSize;
    }

    /**
     * 获取当前页
     * 
     * @return
     */
    public int getCurrentPage(){
        int _currentPage = 0;

        if (this.totalPage == 0)
        {
            _currentPage = 0;
        }
        else
        {
            _currentPage = currentPage;
        }

        return _currentPage;
    }
    /**
     * 设置首页
     * 
     * @param sb
     */
    public void setFirstPage(){
        if (this.currentPage <= 1)
        {
            this.page=1;
        }
        
    }

    /**
     * 设置上页
     * 
     * @param sb
     */
    public void setPrevPage(){
        if (this.currentPage <= 1)
        {
           this.page=1;
        }else{
        	this.page=this.currentPage-1;
        }
        
    }

    /**
     * 设置下页
     * 
     * @param sb
     */
    public void setNextPage(){
        if (this.currentPage < this.totalPage)
        {
          this.page=this.currentPage+1; 
        }
        else
        {
           this.page=this.totalPage;
        }
    }

    /**
     * 设置末页
     * 
     * @param sb
     */
    public void setLastPage(){
       this.page=this.totalPage;
    }

}
