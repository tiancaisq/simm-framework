<template id="list">
  <section class="content-section">
    <el-row :gutter="20" class="search-form el-drawer__body">
      <el-form :model="request" label-width="120px" class="demo-ruleForm">
        <el-col :span="8">
          <el-form-item label="一级类目名称">
            <el-input placeholder="请输入一级类目名称" v-model.trim="request.category_name"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态">
            <el-select v-model="request.status">
              <el-option label="启用" value="1"></el-option>
              <el-option label="禁用" value="0"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label-width="10px">
            <el-button
              type="primary"
              class="btn-default"
              icon="el-icon-search"
              @click="searchFun()"
            >搜索</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div class="search-table">
      <el-button type="primary" @click="addShow()" class="btn-default" icon="el-icon-plus">新增一级类目</el-button>
      <el-button
        v-permission:adjust_category
        type="primary"
        @click="adjust"
        class="btn-default"
        icon="el-icon-setting"
      >类目调整</el-button>
      <el-table border :data="infoData.data" style="width: 100%">
        <el-table-column prop="id" label="一级类目ID"></el-table-column>
        <el-table-column prop="categoryName" label="类目名称"></el-table-column>
        <el-table-column prop="createAt" label="创建时间"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            {{
            scope.row.status == 1 ? "启用" : "禁用"
            }}
          </template>
        </el-table-column>
        <el-table-column prop="grossProfitRate" label="毛利率（%）"></el-table-column>
        <el-table-column prop="afterTime" label="商品售后时效（天）"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <sxh-detail-view comp="categoryView" :view-param="{ id: scope.row.id }"></sxh-detail-view>
            <el-button plain size="minier" @click="editShow(scope.row)">编辑</el-button>
            <router-link :to="'/base/prod/category/list2/' + scope.row.id">
              <el-button plain size="minier">二级分类</el-button>
            </router-link>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        class="pull-right"
        @current-change="handleCurrentChange"
        :current-page="page"
        layout="total, prev, pager, next, jumper"
        :page-size="pagesize"
        :total="infoData.records"
      ></el-pagination>
    </div>
    <el-dialog title="新增一级类目" :visible.sync="addFlg">
      <el-form :model="addData" label-width="25%" :rules="rules" ref="addData">
        <el-form-item label="一级类目名称" prop="categoryName">
          <el-input placeholder="请输入一级类目名称" v-model.trim="addData.categoryName" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model.number="addData.sort" auto-complete="off" type="number"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="addData.status">
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品售后时效" prop="afterTime">
          <el-input
            placeholder="请输入商品售后时效，单位‘天’"
            v-model.number="addData.afterTime"
            auto-complete="off"
            type="number"
          ></el-input>
        </el-form-item>
        <el-form-item label="毛利率" prop="grossProfitRate" title>
          <template slot="label">
            <span>毛利率</span>
          </template>
          <sxh-input-number
                  :precision="2"
                  :step="0.1"
                  :min="0"
                  :controls="false"
                  v-model="addData.grossProfitRate"
                  placeholder="请设置毛利率"
                  auto-complete="off"
          >
            <template slot="append">%</template>
          </sxh-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addSave(addData, 'addData')">确 定</el-button>
        <el-button @click="addFlg = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑类目" :visible.sync="editFlg">
      <el-form :model="selected" label-width="25%" :rules="rules" ref="selected">
        <el-form-item label="类目名称" prop="categoryName">
          <el-input v-model.trim="selected.categoryName" auto-complete="off" disabled></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model.number="selected.sort" auto-complete="off" type="number"></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="selected.status">
            <el-option label="启用" value="1"></el-option>
            <el-option label="禁用" value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="商品售后时效" prop="afterTime">
          <el-input v-model.number="selected.afterTime" auto-complete="off" type="number"></el-input>
        </el-form-item>
        <el-form-item label="毛利率" prop="grossProfitRate" title>
          <template slot="label">
            <span>毛利率</span>
          </template>
          <sxh-input-number
                  :precision="2"
                  :step="0.1"
                  :min="0"
                  :controls="false"
                  v-model="selected.grossProfitRate"
                  placeholder="请设置毛利率"
                  auto-complete="off"
          >
            <template slot="append">%</template>
          </sxh-input-number>
        </el-form-item>
        <el-form-item>
        <h5 style="color: red;">
          注意：<br/>1、如更改，二级类目毛利率及城市毛利率将会同步调整（不包含代理城市）；<br/>2、如毛利率发生变更，正在售卖、待上架、已下架与等待审核的SGU不受影响；
        </h5>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="editSave(selected, 'selected')">确 定</el-button>
        <el-button @click="editFlg = false">取 消</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
export default {
  data: function() {
    return {
      loading: true,
      page: 1,
      pagesize: 10,
      editFlg: false,
      addFlg: false,
      selected: {},
      request: {
        category_name: "",
        status: "",
        afterTime: null
      },
      addData: {
        categoryName: "",
        sort: 0,
        status: "1",
        afterTime: null,
        grossProfitRate:0
      },
      infoData: {},
      rules: {
        categoryName: [
          { required: true, message: "请输入分类名称", trigger: "blur" }
          // {max: 4, message: '分类名称长度不得超过4', trigger: 'blur'}
        ],
        sort: [
          {
            required: true,
            message: "请输入排序(正整数)",
            trigger: "blur",
            type: "number"
          },
          {
            pattern: /^[+]{0,1}(\d+)$/,
            message: "请输入排序(正整数)",
            trigger: "blur"
          }
        ],
        status: [{ required: true, message: "请输入状态", trigger: "change" }],
        afterTime: [
          {
            type: "number",
            required: true,
            message: "请输入商品售后时效(正整数)",
            trigger: "blur"
          },
          {
            validator: this.validateInt,
            trigger: "blur"
          }
        ],
        grossProfitRate: [
          {
            type: "number",
            required: true,
            trigger: "blur",
            label: "毛利率",
            message: "请输入毛利率(大于等于0，最多两位小数)",
          }
        ],
      }
    };
  },
  created: function() {
    this.getListData();
  },
  methods: {
    validateInt(rule, value, callback) {
      var regExp = /^[+]{0,1}(\d+)$/;
      if (regExp.test(value) === false) {
        callback(new Error("请输入商品售后时效(正整数)"));
      } else {
        callback();
      }
    },
    handleCurrentChange(val) {
      this.page = val;
      this.getListData();
    },
    adjust() {
      this.$router.push({ path: "/base/prod/category/adjust" });
    },
    getListData: function() {
      var self = this;
      var store = this.$store;
      this.$parent.callAPI2(
        "GET",
        "/base/prod/category/search.json",
        {
          token: store.state.token,
          tokenid: store.state.tokenid,
          parent_id: 0,
          category_name: self.request.category_name,
          status: self.request.status,
          afterTime: self.request.afterTime,
          page: self.page,
          pagesize: self.pagesize
        },
        function(data) {
          if (data.resultCode === 1000) {
            self.infoData = data;
          }
        }
      );
    },
    searchFun: function() {
      if (this.page === 1) {
        this.getListData();
      } else {
        this.page = 1;
      }
    },
    ajaxFun: function(json, item) {
      var self = this;
      var store = this.$store;
      var jsondata = Object.assign(
        {
          token: store.state.token,
          tokenid: store.state.tokenid,
          id: item.id,
          categoryName: item.categoryName,
          sort: item.sort,
          status: item.status,
          afterTime: item.afterTime,
          grossProfitRate:item.grossProfitRate
        },
        json
      );
      this.$parent.callAPI(
        "POST",
        "/base/prod/category/doUpdate.json",
        jsondata,
        function(data) {
          self.getListData();
          self.editFlg = false;
        }
      );
    },
    addShow: function() {
      this.addData = {
        categoryName: "",
        sort: 0,
        status: "1",
        afterTime: null,
        grossProfitRate:0
      };
      this.addFlg = true;
    },
    addSave: function(item, formName) {
      var self = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          var store = self.$store;
          self.$parent.callAPI(
            "POST",
            "/base/prod/category/doadd.json",
            {
              token: store.state.token,
              tokenid: store.state.tokenid,
              categoryName: item.categoryName,
              refCategoryName: "",
              parentId: 0,
              categoryLevel: 1,
              sort: item.sort,
              status: item.status,
              afterTime: item.afterTime,
              grossProfitRate:item.grossProfitRate
            },
            function(data) {
              self.page = 1;
              self.getListData();
              self.addFlg = false;
            }
          );
        } else {
          return false;
        }
      });
    },
    editShow: function(item) {
      item.status = String(item.status);
      item.sort = Number(item.sort);
      item.afterTime = item.afterTime == null ? null : Number(item.afterTime);
      item.grossProfitRate=item.grossProfitRate == null ? null : Number(item.grossProfitRate);
      this.selected = Object.assign({}, item);
      this.editFlg = true;
    },
    editSave: function(item, formName) {
      var self = this;
      this.$refs[formName].validate(valid => {
        if (valid) {
          self.ajaxFun({}, item);
        } else {
          return false;
        }
      });
    },
    cancelFun: function() {
      this.editFlg = false;
      this.addFlg = false;
    },
  }
};
</script>
