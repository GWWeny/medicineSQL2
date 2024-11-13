<template>
  <el-container>
    <el-main>
      <!-- 首页内容 -->
      <el-row gutter="20" style="margin-top: 20px;">

        <el-col :span="8">
        <el-card class="box-card" :body-style="{ padding: '20px' }">
          <div slot="header" class="clearfix">
            <span>信息数据打印</span>
          </div>
          <div style="text-align: center; display: flex; justify-content: center; gap: 20px;">
            <!-- 使用flexbox水平排列按钮，并设置按钮之间的间隔 -->
            <el-button @click="openMedicineDialog" type="primary" icon="el-icon-plus" style="margin-top: 20px;">药品数据</el-button>
            <el-button @click="openAgencyDialog" type="primary" icon="el-icon-plus" style="margin-top: 20px;">经办人数据</el-button>
            <el-button @click="openClientDialog" type="primary" icon="el-icon-plus" style="margin-top: 20px;">顾客数据</el-button>
          </div>
        </el-card>
      </el-col>
      </el-row>




      <el-dialog
          title="药品信息PDF生成"
          :visible.sync="dialogVisibleMedicine"
          width="900px"
      >
        <div id="pdf-content-medicine">
          <el-table :data="medicines" style="width: 100%; margin-top: 20px;">
            <el-table-column prop="mno" label="药品编号" width="225"></el-table-column>
            <el-table-column prop="mname" label="药品名称" width="225"></el-table-column>
            <el-table-column prop="mmode" label="服用方法" width="225"></el-table-column>
            <el-table-column prop="mefficacy" label="药效" width="225"></el-table-column>
          </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleMedicine = false">取 消</el-button>
        <el-button type="primary" @click="generatePDF('medicine')">生成PDF</el-button>
      </span>
      </el-dialog>

      <el-dialog
          title="经办人信息PDF生成"
          :visible.sync="dialogVisibleAgency"
          width="750px"
      >
        <div id="pdf-content-agency">
          <el-table :data="agencys" style="width: 100%; margin-top: 20px;" >
            <el-table-column prop="ano" label="编号" width="150"></el-table-column>
            <el-table-column prop="aname" label="姓名" width="150"></el-table-column>
            <el-table-column prop="asex" label="性别" width="150"></el-table-column>
            <el-table-column prop="aphone" label="电话" width="150"></el-table-column>
            <el-table-column prop="aremark" label="备注" width="150"></el-table-column>
          </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAgency = false">取 消</el-button>
        <el-button type="primary" @click="generatePDF('agency')">生成PDF</el-button>
      </span>
      </el-dialog>

      <el-dialog
          title="顾客信息PDF生成"
          :visible.sync="dialogVisibleClient"
          width="800px"
      >
        <div id="pdf-content-client">
          <el-table :data="clients" style="width: 100%; margin-top: 20px;" >
            <el-table-column prop="cno" label="编号" width="70"></el-table-column>
            <el-table-column prop="cname" label="姓名" width="70"></el-table-column>
            <el-table-column prop="csex" label="性别" width="70"></el-table-column>
            <el-table-column prop="cage" label="年龄" width="70"></el-table-column>
            <el-table-column prop="cphone" label="电话" width="70"></el-table-column>
            <el-table-column prop="caddress" label="地址" width="70"></el-table-column>
            <el-table-column prop="csymptom" label="症状" width="70"></el-table-column>
            <el-table-column prop="cdate" label="时间" width="70"></el-table-column>
            <el-table-column prop="cremark" label="备注" width="70"></el-table-column>
            <el-table-column prop="mno" label="已购药品编号" width="70"></el-table-column>
            <el-table-column prop="ano" label="经办人编号" width="70"></el-table-column>
          </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleClient = false">取 消</el-button>
        <el-button type="primary" @click="generatePDF('client')">生成PDF</el-button>
      </span>
      </el-dialog>

    </el-main>
  </el-container>
</template>


<script>
import html2pdf from 'html2pdf.js';
import request from "@/utils/request";
export default {
  name: 'HomeView',
  data() {
    return {
      medicines:[],
      agencys: [],
      clients:[],
      dialogVisibleMedicine: false,
      dialogVisibleAgency: false,
      dialogVisibleClient: false,
      gutter: 20,// 确保是数字
      localStorageUser: JSON.parse(localStorage.getItem('localStorageUser') || '{}'),
    };
  },
  methods: {
    findMedicineAll() {
      request.get('/medicine/findAll')
          .then(response => {
            console.log('response.data.data:',response.data.data);
            if(response.data.code === "200"){
              this.$message.success("获取药品数据成功");
              console.log('获取药品数据:', response.msg);
              this.medicines = response.data.data.sort((a, b)=>a.mno-b.mno);
            }else{
              this.$message.warning("没有药品数据");
              this.medicines = [];
            }

          }).catch(error => {
        this.$message.error("获取药品数据失败");
        this.medicines = [];
      });
    },
    findAgencyAll() {
      request.get('/agency/findAll')
          .then(response => {
            console.log('response.data.data:',response.data.data);
            if(response.data.code === "200"){
              this.$message.success("获取经办人数据成功");
              console.log('获取药品数据:', response.msg);
              this.agencys = response.data.data.sort((a, b)=>a.ano-b.ano);
            }else{
              this.$message.warning("没有经办人数据");
              this.agencys = [];
            }

          }).catch(error => {
        this.$message.error("获取药品数据失败");
        this.agencys = [];
      });
    },
    findClientAll() {
      request.get('/client/findAll')
          .then(response => {
            console.log('response.data.data:',response.data.data);
            if(response.data.code === "200"){
              this.$message.success("获取顾客数据成功");
              console.log('获取顾客数据:', response.msg);
              this.clients = response.data.data.sort((a, b)=>a.cno-b.cno);
            }else{
              this.$message.warning("没有药品数据");
              this.clients = [];
            }

          }).catch(error => {
        this.$message.error("获取药品数据失败");
        this.clients = [];
      });
    },
    // 生成PDF的函数
    async generatePDF(type) {
      let element;
      if (type === 'medicine') {
        element = document.getElementById('pdf-content-medicine');  // 对应药品数据的 div
      } else if (type === 'agency') {
        element = document.getElementById('pdf-content-agency');  // 对应经办人数据的 div
      } else if (type === 'client') {
        element = document.getElementById('pdf-content-client');  // 对应顾客数据的 div
      }
      if (!element) {
        this.$message.error("无法找到PDF内容");
        return;
      }
      // 配置pdf生成选项
      const options = {
        margin:       1,
        filename:     'list.pdf',
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { dpi: 192, scale: 2 },
        jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait',compress: true }
      };

      // 生成并下载PDF
      html2pdf().from(element).set(options).save();
      this.dialogVisibleMedicine=false;
      this.dialogVisibleClient=false;
      this.dialogVisibleAgency=false;
    },

    openMedicineDialog(){
      this.dialogVisibleMedicine=true;
    },
    openAgencyDialog(){
      this.dialogVisibleAgency=true;
    },
    openClientDialog(){
      this.dialogVisibleClient=true;
    },
  },
  mounted(){
    this.findMedicineAll();
    this.findAgencyAll();
    this.findClientAll();
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
