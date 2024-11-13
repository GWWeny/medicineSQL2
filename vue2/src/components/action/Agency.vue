<template>
  <el-container>
    <!-- Header Section -->
    <el-header>
      <el-row>
        <el-col :span="12" class="text-right">
          <el-button @click="findAll" type="primary" icon="el-icon-search">查询全部</el-button>
        </el-col>
      </el-row>
    </el-header>

    <el-main>
      <el-form :model="searchParams" inline class="findAll-container">
        <el-form-item label="编号" prop="ano">
          <el-input  v-model="searchParams.ano" placeholder="按编号搜索"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="aname">
          <el-input v-model="searchParams.aname" placeholder="按姓名搜索"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="asex">
          <el-select v-model="searchParams.asex" placeholder="按性别搜索">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="aphone">
          <el-input v-model="searchParams.aphone" placeholder="按电话搜索"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="aremark">
          <el-input v-model="searchParams.aremark" placeholder="按备注搜索"></el-input>
        </el-form-item>
        <el-button @click="Search" type="primary" icon="el-icon-search">搜索</el-button>
        <el-button @click="resetSearch" type="warning" icon="el-icon-refresh">重置</el-button>
      </el-form>

      <el-table :data="agencys" style="width: 100%; margin-top: 20px;" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="ano" label="编号" width="250"></el-table-column>
        <el-table-column prop="aname" label="姓名" width="250"></el-table-column>
        <el-table-column prop="asex" label="性别" width="250"></el-table-column>
        <el-table-column prop="aphone" label="电话" width="250"></el-table-column>
        <el-table-column prop="aremark" label="备注" width="250"></el-table-column>
        <el-table-column label="操作" width="200">
          <template v-slot="scope">
            <el-button v-if="isAdmin" @click="updateAgency(scope.row)" type="primary" icon="el-icon-edit">编辑</el-button>
            <el-button v-if="isAdmin" @click="deleteAgency(scope.row)" type="danger" icon="el-icon-delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-button v-if="isAdmin" @click="openAddDialog" type="success" icon="el-icon-plus" style="margin-top: 20px;">添加药品</el-button>
      <el-button v-if="isAdmin" @click="DeleteBatchAgency" style="margin-top: 40px;" >批量删除</el-button>
    </el-main>

    <!-- Add Agency Dialog -->
    <el-dialog
        title="添加经办人信息"
        :visible.sync="dialogVisibleAdd"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="newAgency" ref="form" label-width="100px">
        <el-form-item label="编号" prop="ano" :rules="[{ required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="newAgency.ano" placeholder="请输入编号" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="aname" :rules="[{ required: true, message: '请输入姓名', trigger: 'blur' }]">
          <el-input v-model="newAgency.aname" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="asex" :rules="[{ required: true, message: '请输入性别', trigger: 'blur' }]">
          <el-select v-model="newAgency.asex" required placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="aphone" :rules="[{ required: true, message: '请输入电话', trigger: 'blur' }]">
          <el-input v-model="newAgency.aphone" placeholder="请输入电话"></el-input>
        </el-form-item>

        <el-form-item label="备注" prop="aremark" :rules="[{ required: true, message: '请输入备注', trigger: 'blur' }]">
          <el-input v-model="newAgency.aremark" placeholder="请输入备注"></el-input>
        </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAdd = false">取 消</el-button>
        <el-button type="primary" @click="addAgencyConfirm">确 定</el-button>
      </span>

    </el-dialog>

    <!-- Update Agency Dialog -->
    <el-dialog
        title="修改经办人信息"
        :visible.sync="dialogVisibleUpdate"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="newAgency" ref="form" label-width="100px">
        <el-form-item label="编号" prop="ano" >
          <el-input v-model="newAgency.ano" disabled placeholder="请输入编号" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="aname" >
          <el-input v-model="newAgency.aname" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="asex" >
          <el-select v-model="newAgency.asex" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="aphone" >
          <el-input v-model="newAgency.aphone" placeholder="请输入电话"></el-input>
        </el-form-item>

        <el-form-item label="备注" prop="aremark" >
          <el-input v-model="newAgency.aremark" placeholder="请输入备注"></el-input>
        </el-form-item>


      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdate = false">取 消</el-button>
        <el-button type="primary" @click="UpdateAgency">确 定</el-button>
      </span>

    </el-dialog>

  </el-container>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      agencys: [],
      loading: false,
      noResults: false,
      searchParams: {
        ano: '',
        aname: '',
        asex: '',
        aphone: '',
        aremark:''
      },
      isAdmin: false,  // 用来判断当前用户是否有管理员权限
      dialogVisibleAdd: false,  // 控制添加药品对话框的显示与隐藏
      dialogVisibleUpdate:false,//控制修改药品对话框的显示与隐藏
      SelectionAgencys:[],   //已选中的药品
      newAgency: {        // 添加药品表单的数据模型
        ano: '',
        aname: '',
        asex: '',
        aphone: '',
        aremark:''
      },
    };
  },
  methods: {
    // 查询所有药品
    findAll() {
      request.get('/agency/findAll')
          .then(response => {
            console.log('response.data.data:',response.data.data);
            if(response.data.code === "200"){
              this.$message.success("获取药品数据成功");
              console.log('获取药品数据:', response.msg);
              this.agencys = response.data.data.sort((a, b)=>a.ano-b.ano);
            }else{
              this.$message.warning("没有药品数据");
              this.agencys = [];
            }

          }).catch(error => {
        this.$message.error("获取药品数据失败");
        this.agencys = [];
      });
    },

    //搜索
    Search() {
      const arr={...this.searchParams};
      Object.keys(arr).forEach(key => {
        if (!arr[key]) {
          delete arr[key];
        }
      });
      this.loading=true;
      this.noResults=false;

      request.get('/agency/search',{params:arr})
          .then(response=>{
            const {code,msg}=response.data;
            if(code==='500'){
              this.$message.warning(msg);
              return;
            }

            console.log('response.data.data:', response.data.data);
            this.agencys=response.data.data;
            console.log('搜索结果:', this.agencys)
            this.noResults=this.agencys.length===0;
            this.$message.success('搜索成功');
            this.loading=false;
          }).catch(error=>{
        this.loading=false;
        this.$message.error('搜索失败');
        this.agencys=[];
        this.noResults=true;
      })
    },
    //重置搜索
    resetSearch(){
      this.searchParams = {
        ano: '',
        aname: '',
        asex: '',
        aphone: '',
        aremark:''
      };
      this.findAll();
      this.noResults=false;
    },


    // 编辑药品
    UpdateAgency() {
      this.$refs.form.validate(valid => {
        if (valid) {
          request.put(`/agency/update/${this.newAgency.ano}`, this.newAgency)
              .then(response => {
                const{code,msg}=response.data;
                if(code==='401'){
                  this.$message.error(msg);
                  return;
                }
                this.dialogVisibleUpdate = false;  // 关闭对话框
                this.findAll();// 刷新药品列表
                this.$message.success('添加成功');
              })
              .catch(error => {
                console.error(error);
                this.$message.error('添加失败');
              });
        } else {
          return false;
        }
      });
    },


    // 删除药品
    deleteAgency(agency) {
      this.$confirm('确认删除该药品?', '删除提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete(`/agency/delete/${agency.ano}`)
            .then((response) => {
              const {code,msg}=response.data;
              if(code==='401'){
                this.$message.error(msg);
              }else{
                this.findAll();  // 删除成功后重新查询
                this.$message.success('删除成功');
              }
            })
            .catch(error => {
              this.$message.error('删除失败');
            });
      }).catch(()=>{
        this.$message.info("已取消删除的操作")
      });
    },
    //选中的要删除的数据
    handleSelectionChange(selection){
      this.SelectionAgencys=selection;
    },
    //批量删除药品
    DeleteBatchAgency() {
      if(this.SelectionAgencys.length===0){
        this.$message.warning("请选择要删除的药品");
        return;
      }
      this.$confirm(`确认要删除选中的${this.SelectionAgencys.length}个药品？`,'删除提示',{
        type:'warning'
      }).then(()=>{
        const agencyAnos=this.SelectionAgencys.map(item=>item.ano);//获取选中药品的编号
        this.$request.delete('/agency/deleteBatch',{data:agencyAnos})//发送批量删除请求
            .then(response=>{
              console.log("我到这了");
              const {code,msg}=response.data;
              if(code === "200"){
                this.$message.success("批量删除成功");
                this.findAll();
              }else{
                this.$message.error(msg);
              }
            }).catch(error=>{
          console.error(error);
          const errorMsg = error.response?.data?.msg || "批量删除失败";
          this.$message.error(errorMsg);
        });
      }).catch(()=>{
        this.$message.info("已取消批量删除的操作");
      });
    },

    // 打开添加药品对话框
    openAddDialog() {
      this.dialogVisibleAdd = true;
    },
    //打开修改药品对话框
    updateAgency(agency){
      this.newAgency={...agency}
      this.dialogVisibleUpdate=true;
    },
    // 添加药品确认
    addAgencyConfirm() {
      this.$refs.form.validate(valid => {
        console.log("this.newAgency",this.newAgency);
        if (valid) {
          request.post('/agency/add', this.newAgency)
              .then(response => {
                const{code,msg}=response.data;
                if(code==='401'){
                  this.$message.error(msg);
                  return;
                }
                this.dialogVisibleAdd = false;  // 关闭对话框
                this.findAll();               // 刷新药品列表
                this.$message.success('添加成功');
              });
        } else {
          return false;
        }
      });
    },
    // 重置表单
    resetForm() {
      console.log("重置前:", this.newAgency);
      //this.$refs.form.resetFields();
      this.newAgency = {ano: '', aname: '', asex: '', aphone: '',aremark:''};
      console.log("重置后:", this.newAgency);
    }
  },
  //挂载钩子，自动加载
  mounted() {

    // 假设管理员信息保存在本地存储中
    let user=JSON.parse(localStorage.getItem('localStorageUser') || '{}');
    this.isAdmin = user.data.role ==='admin';
    console.log('isAdmin:', this.isAdmin);  // 查看管理员权限是否正确设置
    this.findAll();  // 组件加载时默认查询所有药品
  }
};
</script>

<style scoped>
.text-right {
  text-align: right;
}
</style>
