<template>
  <el-container>

    <el-main>
      <!-- 首页内容 -->
      <el-row gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card class="box-card" :body-style="{ padding: '20px' }">
            <div slot="header" class="clearfix">
              <span>系统概览</span>
            </div>
            <div style="text-align: center;">
              <el-button type="primary" @click="generatePDF">生成PDF</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="box-card" :body-style="{ padding: '20px' }">
            <div slot="header" class="clearfix">
              <span>近期数据</span>
            </div>
            <div style="text-align: center;">
              <el-button type="primary" @click="generatePDF">生成PDF</el-button>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card class="box-card" :body-style="{ padding: '20px' }">
            <div slot="header" class="clearfix">
              <span>用户统计</span>
            </div>
            <div style="text-align: center;">
              <el-button type="primary" @click="generatePDF">生成PDF</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 这里可以加入更多的内容块 -->
    </el-main>
  </el-container>
</template>

<script>
import html2pdf from 'html2pdf.js';
export default {
  name: 'HomeView',
  data() {
    return {
      localStorageUser: JSON.parse(localStorage.getItem('localStorageUser') || '{}'),
    };
  },
  methods: {
    // 生成PDF的函数
    generatePDF() {
      const element = document.getElementById('pdf-content'); // 获取需要生成PDF的HTML元素

      // 配置pdf生成选项
      const options = {
        margin:       1,
        filename:     'system_overview.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { dpi: 192, scale: 2 },
        jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait' }
      };

      // 生成并下载PDF
      html2pdf().from(element).set(options).save();
    }
  }
};
</script>

<style scoped>
.el-header {
  background-color: #26a69a;
  color: white;
  padding: 0 20px;
}

.el-menu-item {
  font-size: 16px;
}

.el-menu-item:hover {
  background-color: #1890ff;
}

.el-dropdown-menu {
  background-color: #26a69a;
  color: white;
}

.box-card {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.el-button {
  margin-top: 10px;
}
</style>
