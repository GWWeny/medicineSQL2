<template>
  <div class="home-container">
    <!-- 信息浏览模块 -->
    <el-tabs v-model="activeTab" class="home-tabs" style="border-bottom: 2px solid #409EFF;">
      <el-tab-pane label="信息浏览" name="browse">
        <div class="section">
          <el-row :gutter="20">
            <el-button type="primary" @click="openBrowse('client')" class="action-button">浏览顾客信息</el-button>
            <el-button type="primary" @click="openBrowse('agency')" class="action-button">浏览经办人信息</el-button>
            <el-button type="primary" @click="openBrowse('medicine')" class="action-button">浏览药品信息</el-button>
          </el-row>
          <div class="table-container">
            <el-table :data="currentTableData" border style="width: 100%" v-loading="loading" class="custom-table">
              <el-table-column prop="id" label="ID" width="80"></el-table-column>
              <el-table-column prop="name" label="名称" width="150"></el-table-column>
              <el-table-column prop="details" label="详情" width="300"></el-table-column>
              <el-table-column label="操作" width="120">
                <template slot-scope="scope">
                  <el-button size="mini" @click="viewDetails(scope.row)" class="view-button">查看</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>

      <!-- 数据报表模块 -->
      <el-tab-pane label="数据报表" name="report">
        <div class="section">
          <el-row :gutter="20">
            <el-button type="primary" @click="generateReport('client')" class="action-button">顾客信息报表</el-button>
            <el-button type="primary" @click="generateReport('agency')" class="action-button">经办人信息报表</el-button>
            <el-button type="primary" @click="generateReport('medicine')" class="action-button">药品信息报表</el-button>
          </el-row>
          <div class="report-container">
            <div v-if="reportData && reportData.length > 0">
              <el-table :data="reportData" border style="width: 100%" v-loading="loading" class="custom-table">
                <el-table-column prop="id" label="ID" width="80"></el-table-column>
                <el-table-column prop="name" label="名称" width="150"></el-table-column>
                <el-table-column prop="details" label="详情" width="300"></el-table-column>
              </el-table>
              <el-button type="success" @click="exportReport" style="margin-top: 20px;" class="export-button">导出报表</el-button>
            </div>
            <div v-else>
              <el-alert title="没有数据" type="info" description="没有可显示的报表数据。" show-icon></el-alert>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 信息查询模块 -->
      <el-tab-pane label="信息查询" name="search">
        <div class="section">
          <el-row :gutter="20">
            <el-input v-model="searchQuery" placeholder="请输入查询关键词" clearable class="search-input"></el-input>
            <el-button type="primary" @click="search" class="search-button">查询</el-button>
            <el-button @click="resetSearch" class="reset-button">重置</el-button>
          </el-row>
          <div class="search-table">
            <el-table :data="searchResults" border style="width: 100%" v-loading="loading" class="custom-table">
              <el-table-column prop="id" label="ID" width="80"></el-table-column>
              <el-table-column prop="name" label="名称" width="150"></el-table-column>
              <el-table-column prop="details" label="详情" width="300"></el-table-column>
            </el-table>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import request from '@/utils/request';

export default {
  name: 'SystemHomePage',
  data() {
    return {
      activeTab: 'browse', // 当前激活的tab页
      loading: false, // 加载状态
      searchQuery: '', // 查询关键字
      currentTableData: [], // 当前浏览的数据（客户、经办人、药品）
      reportData: [], // 报表数据
      searchType: 'client', // 默认查询类型（如 client、agency、medicine）
      searchResults: [],  // 查询结果
    };
  },
  methods: {
    // 打开浏览数据
    openBrowse(type) {
      this.loading = true;

      // 动态生成请求路径
      const apiPath = `/${type}/findAll`;  // 用 `${type}` 动态拼接接口路径

      console.log('请求路径：', apiPath);
      console.log('打开浏览数据，类型：', type);

      request.get(apiPath) // 请求浏览数据的API
          .then(response => {
            console.log('API 返回数据:', response.data);  // 打印返回数据，检查数据是否正确

            if (response.data) {
              this.currentTableData = response.data;  // 将返回的数据赋值给 currentTableData
            }
          })
          .catch(error => {
            this.loading = false;
            console.error('请求失败', error);  // 记录错误
            this.$message.error('数据加载失败');
          });
    },


    // 查看详细信息
    viewDetails(row) {
      this.$message.info(`查看 ${row.name} 的详情`);
    },

    // 生成报表
    generateReport(type) {
      this.loading = true;
      request.get(`/data/${type}/report`) // 请求报表数据的API（客户、经办人、药品等）
          .then(response => {
            this.loading = false;
            if (response.data) {
              this.reportData = response.data;
            }
          })
          .catch(() => {
            this.loading = false;
            this.$message.error('报表生成失败');
          });
    },

    // 导出报表
    exportReport() {
      this.$message.success('导出报表成功');
      // 可以使用 jsPDF 或其他库来生成PDF报告
    },

    // 进行搜索
    search() {
      this.loading = true;
      let apiPath = '';  // 请求路径
      // 根据选择的 searchType 动态选择接口路径
      switch (this.searchType) {
        case 'agency':
          apiPath = '/agency/search';
          break;
        case 'medicine':
          apiPath = '/medicine/search';
          break;
        case 'client':
          apiPath = '/client/search';
          break;
      }

      // 发起请求
      request
          .get(apiPath, { params: { query: this.searchQuery } })
          .then((response) => {
            this.loading = false;
            if (response.data) {
              this.searchResults = response.data;
            }
          })
          .catch(() => {
            this.loading = false;
            this.$message.error('查询失败');
          });
    },

    // 重置搜索
    resetSearch() {
      this.searchQuery = '';  // 清空搜索框
      this.searchResults = [];  // 清空搜索结果
      this.searchType = 'client';  // 重置为默认的搜索类型
    }
  }
};
</script>

<style scoped>
.home-container {
  padding: 20px;
  background-color: #f9f9f9;
}

.home-tabs {
  background-color: #fff;
}

.section {
  margin-top: 20px;
}

.el-row {
  margin-bottom: 20px;
}

.table-container, .report-container, .search-table {
  margin-top: 20px;
}

.el-button {
  margin-right: 10px;
  padding: 10px 20px;
  font-size: 14px;
}

.el-table .cell {
  padding: 10px;
}

.el-alert {
  margin-top: 20px;
}

.el-table th {
  background-color: #f5f5f5;
  color: #333;
}

.el-table td {
  background-color: #fff;
}

.el-dialog__header {
  background-color: #409EFF;
  color: #fff;
}

.el-form-item {
  margin-bottom: 20px;
}

/* Custom styles for better UI */
.action-button {
  min-width: 160px;
}

.view-button {
  background-color: #409EFF;
  color: white;
  border-color: #409EFF;
}

.export-button {
  width: 200px;
  margin-top: 20px;
}

.search-input {
  width: 250px;
}

.search-button, .reset-button {
  margin-left: 10px;
  font-size: 14px;
}

.search-button {
  background-color: #67C23A;
  color: white;
}

.reset-button {
  background-color: #f56c6c;
  color: white;
}
</style>
