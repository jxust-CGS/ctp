package cn.jxust.paper.service;

import cn.jxust.paper.model.Paper;

import com.jfinal.plugin.activerecord.Page;

public class PaperService
{

	public String getTypeList_json()
	{
		String json="[" +
          "{" +
            "'text': 'Parent 9999999'," +
            "'nodes': [" +
              "{" +
                "'text': 'Child 1'," +
                "'nodes': [" +
                  "{" +
                    "'text': 'Grandchild 1'" +
                  "}," +
                  "{" +
                    "'text': 'Grandchild 2'" +
                  "}" +
                "]" +
              "}," +
              "{" +
                "'text': 'Child 2'" +
              "}" +
            "]" +
          "}," +
          "{" +
            "'text': 'Parent 2'" +
          "}," +
          "{" +
            "'text': 'Parent 3'" +
          "}," +
          "{" +
            "'text': 'Parent 4'" +
          "}," +
          "{" +
            "'text': 'Parent 5'" +
          "}" +
        "]";
		return json;
	}

	public Page<Paper> findPage_paper(int pageNumber, int pageSize)
	{
	return Paper.dao.paginate(pageNumber, pageSize, "select *",
			"from paper order by createtime desc");
	}

}
