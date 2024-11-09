<template>
  <div class="agency-container">
    <!-- 搜索部分 -->
    <el-row class="search-row" type="flex" justify="space-between" align="middle">
      <el-col :span="6">
        <el-input v-model="searchForm.aname" placeholder="请输入经办人姓名" clearable></el-input>
      </el-col>
      <el-col :span="6">
        <el-input v-model="searchForm.aphone" placeholder="请输入联系电话" clearable></el-input>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="handleSearch" :loading="loading">搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </el-col>
    </el-row>

    <!-- 表格显示部分 -->
    <el-table :data="agencys" style="width: 100%" v-loading="loading" border>
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="ano" label="编号" width="120"></el-table-column>
      <el-table-column prop="aname" label="姓名" width="150"></el-table-column>
      <el-table-column prop="asex" label="性别" width="100"></el-table-column>
      <el-table-column prop="aphone" label="电话" width="150"></el-table-column>
      <el-table-column prop="aremark" label="备注" width="200"></el-table-column>
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" @click="editAgency(scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="deleteAgency(scope.row.ano)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 操作按钮 -->
    <div class="action-buttons">
      <el-button type="danger" @click="deleteSelectedAgencies" :disabled="multipleSelection.length === 0">批量删除</el-button>
      <el-button @click="reorderAgency">重新排序</el-button>
      <el-button type="success" @click="exportToPDF">导出 PDF</el-button>
    </div>

    <!-- 弹窗表单 -->
    <el-dialog :visible.sync="dialogVisible" title="编辑经办人" :before-close="resetForm">
      <el-form :model="currentAgency" ref="form" label-width="100px">
        <el-form-item label="编号">
          <el-input v-model="currentAgency.ano" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="currentAgency.aname" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="currentAgency.asex">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="currentAgency.aphone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="currentAgency.aremark" placeholder="请输入备注" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveAgency">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request';
import jsPDF from 'jspdf';
import 'jspdf-autotable';

export default {
  name: 'TransactorManagement',
  data() {
    return {
      agencys: [],
      clients: [],
      dialogVisible: false,
      isEditing: false,
      loading: false, // 搜索加载状态
      noResults: false, // 无结果状态
      currentAgency: {
        ano: null,
        aname: '',
        asex: '',
        aphone: '',
        aremark: ''
      },
      searchForm: { // 搜索表单
        ano: '',
        aname: '',
        asex: '',
        aphone: '',
        aremark: ''
      },
      multipleSelection: [] // 存储选中的记录
    };
  },
  methods: {
    async handleSearch() {
      this.loading = true;
      this.noResults = false;

      try {
        const query = this.cleanSearchQuery(this.searchForm);
        const response = await request.get('/agency/search', { params: query });

        this.agencys = response.data;
        this.noResults = this.agencys.length === 0;
      } catch (error) {
        this.handleError('查询失败，请稍后再试');
      } finally {
        this.loading = false;
      }
    },

    cleanSearchQuery(query) {
      return Object.keys(query).reduce((acc, key) => {
        if (query[key]) acc[key] = query[key];
        return acc;
      }, {});
    },

    resetSearch() {
      this.searchForm = {
        ano: '',
        aname: '',
        asex: '',
        aphone: '',
        aremark: ''
      };
      this.fetchAgency();
      this.noResults = false;
    },

    async fetchAgency() {
      try {
        const response = await request.get('/agency/findAll');
        this.agencys = response.data.sort((a, b) => a.ano - b.ano);
      } catch (error) {
        this.handleError('获取经办人数据失败');
      }
    },

    async fetchClient() {
      try {
        const response = await request.get('/client/findAll');
        this.clients = response.data.sort((a, b) => a.cno - b.cno);
      } catch (error) {
        this.handleError('获取客户数据失败');
      }
    },

    handleError(message) {
      this.$message.error(message);
      this.agencys = [];
      this.noResults = true;
    },

    saveAgency() {
      const method = this.isEditing ? 'put' : 'post';
      const url = this.isEditing ? `/agency/update/${this.currentAgency.ano}` : '/agency/save';

      request[method](url, this.currentAgency)
          .then(() => {
            this.dialogVisible = false;
            this.$nextTick(() => {
              this.fetchAgency();
              this.resetForm();
            });
          })
          .catch(this.handleError);
    },

    editAgency(agency) {
      this.currentAgency = { ...agency };
      this.isEditing = true;
      this.dialogVisible = true;
    },

    deleteAgency(ano) {
      if (this.clients.some(client => client.ano === ano)) {
        this.$message.warning('该经办人已关联到客户，无法删除');
        return;
      }

      this.$confirm('确认删除此经办人吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete(`/agency/delete/${ano}`)
            .then(() => {
              this.fetchAgency();
              this.$message.success('删除成功');
            })
            .catch(this.handleError);
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },

    async deleteSelectedAgencies() {
      const nonDeletableAgencies = this.multipleSelection.filter(item =>
          this.clients.some(client => client.ano === item.ano)
      );

      if (nonDeletableAgencies.length > 0) {
        this.$message.warning('选中的经办人中存在不可删除的项目');
        return;
      }

      try {
        const anos = this.multipleSelection.map(item => item.ano);
        const response = await request.delete('/agency/deleteBatch', { data: anos });
        if (response.code === '200') {
          this.$message.success('批量删除成功');
          this.multipleSelection = [];
          this.fetchAgency();
        } else {
          this.$message.error(`删除失败: ${response.msg}`);
        }
      } catch (error) {
        this.handleError('删除失败，请重试');
      }
    },

    reorderAgency() {
      const updatedAgencies = this.agencys.map((agency, index) => ({
        ...agency,
        ano: index + 1
      }));

      request.put('/agency/reorder', updatedAgencies)
          .then(() => {
            this.$message.success('编号重新排序成功');
            this.fetchAgency();
          })
          .catch(this.handleError);
    },

    resetForm() {
      this.currentAgency = {
        ano: null,
        aname: '',
        asex: '',
        aphone: '',
        aremark: ''
      };
      this.isEditing = false;
    },

    exportToPDF() {
      const doc = new jsPDF();
      doc.setFont('dengl');
      doc.text("经办人表单数据", 10, 10);

      const columns = ["ano", "aname", "asex", "aphone", "aremark"];
      const rows = this.agencys.map(agency => [
        agency.ano,
        agency.aname,
        agency.asex,
        agency.aphone,
        agency.aremark
      ]);

      doc.autoTable({
        head: [columns],
        body: rows,
        styles: { font: 'dengl', fontStyle: 'normal' }
      });

      doc.save('经办人表单数据.pdf');
    }
  },
  mounted() {
    this.fetchAgency();
    this.fetchClient();
  }
};
</script>


<style scoped>
.agency-container {
  padding: 20px;
  background-color: #f9f9f9;
}

.search-row {
  margin-bottom: 20px;
}

.action-buttons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.action-buttons .el-button {
  min-width: 120px;
}

.el-table th {
  background-color: #f5f5f5;
  color: #333;
}

.el-table td {
  background-color: #fff;
}

.el-table-column .el-button {
  margin-right: 10px;
}

.dialog-footer {
  text-align: right;
}

.el-dialog__header {
  background-color: #409EFF;
  color: #fff;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item label {
  font-size: 14px;
  font-weight: 500;
}

.el-input, .el-radio-group {
  width: 100%;
}

.el-radio-group .el-radio {
  margin-right: 10px;
}

.el-button {
  height: 32px;
}

.el-table__row:hover {
  background-color: #f5f5f5;
}

.el-table .cell {
  padding: 10px;
}

.el-dialog__header {
  background-color: #f5f5f5;
}

.el-dialog .el-button--primary {
  background-color: #409EFF;
  border-color: #409EFF;
}

.el-dialog .el-button--default {
  background-color: #f5f5f5;
}
</style>

