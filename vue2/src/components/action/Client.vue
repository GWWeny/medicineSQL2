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
        <el-form-item label="编号" prop="cno">
          <el-input  v-model="searchParams.cno" placeholder="按编号搜索"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="cname">
          <el-input v-model="searchParams.cname" placeholder="按姓名搜索"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="csex">
          <el-select v-model="searchParams.csex" placeholder="按性别搜索">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="cage">
          <el-input v-model="searchParams.cage" placeholder="按年龄搜索"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="cphone">
          <el-input v-model="searchParams.cphone" placeholder="按电话搜索"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="caddress">
          <el-input v-model="searchParams.caddress" placeholder="按地址搜索"></el-input>
        </el-form-item>
        <el-form-item label="症状" prop="csymptom">
          <el-input v-model="searchParams.csymptom" placeholder="按症状搜索"></el-input>
        </el-form-item>
        <el-form-item label="时间" prop="cdate">
          <el-input v-model="searchParams.cdate" placeholder="按时间搜索"></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="cremark">
          <el-input v-model="searchParams.cremark" placeholder="按备注搜索"></el-input>
        </el-form-item>
        <el-form-item label="已购药品编号" prop="mno">
          <el-input v-model="searchParams.mno" placeholder="按已购药品编号搜索"></el-input>
        </el-form-item>
        <el-form-item label="经办人编号" prop="ano">
          <el-input v-model="searchParams.ano" placeholder="按经办人编号搜索"></el-input>
        </el-form-item>
        <el-button @click="Search" type="primary" icon="el-icon-search">搜索</el-button>
        <el-button @click="resetSearch" type="warning" icon="el-icon-refresh">重置</el-button>
      </el-form>

      <el-table :data="clients" style="width: 100%; margin-top: 20px;" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="cno" label="编号" width="100"></el-table-column>
        <el-table-column prop="cname" label="姓名" width="100"></el-table-column>
        <el-table-column prop="csex" label="性别" width="100"></el-table-column>
        <el-table-column prop="cage" label="年龄" width="100"></el-table-column>
        <el-table-column prop="cphone" label="电话" width="100"></el-table-column>
        <el-table-column prop="caddress" label="地址" width="100"></el-table-column>
        <el-table-column prop="csymptom" label="症状" width="100"></el-table-column>
        <el-table-column prop="cdate" label="时间" width="100"></el-table-column>
        <el-table-column prop="cremark" label="备注" width="100"></el-table-column>
        <el-table-column prop="mno" label="已购药品编号" width="100"></el-table-column>
        <el-table-column prop="ano" label="经办人编号" width="150"></el-table-column>
        <el-table-column label="操作" width="200">
          <template v-slot="scope">
            <el-button v-if="isAdmin" @click="updateClient(scope.row)" type="primary" icon="el-icon-edit">编辑</el-button>
            <el-button v-if="isAdmin" @click="deleteClient(scope.row)" type="danger" icon="el-icon-delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-button v-if="isAdmin" @click="openAddDialog" type="success" icon="el-icon-plus" style="margin-top: 20px;">添加药品</el-button>
      <el-button v-if="isAdmin" @click="DeleteBatchClient" style="margin-top: 40px;" >批量删除</el-button>
    </el-main>

    <!-- Add Client Dialog -->
    <el-dialog
        title="添加顾客信息"
        :visible.sync="dialogVisibleAdd"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="newClient" ref="form" label-width="100px">
        <el-form-item label="编号" prop="cno" :rules="[{ required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="newClient.cno" placeholder="请输入编号" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="cname" :rules="[{ required: true, message: '请输入姓名', trigger: 'blur' }]">
          <el-input v-model="newClient.cname" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="csex" :rules="[{ required: true, message: '请输入性别', trigger: 'blur' }]">
          <el-select v-model="newClient.csex" required placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="cage" :rules="[{ required: true, message: '请输入年龄', trigger: 'blur' }]">
          <el-input v-model="newClient.cage" placeholder="请输入年龄"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="cphone" :rules="[{ required: true, message: '请输入电话', trigger: 'blur' }]">
          <el-input v-model="newClient.cphone" placeholder="请输入电话" ></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="caddress" :rules="[{ required: true, message: '请输入地址', trigger: 'blur' }]">
          <el-input v-model="newClient.caddress" placeholder="请输入地址" ></el-input>
        </el-form-item>
        <el-form-item label="症状" prop="csymptom" :rules="[{ required: true, message: '请输入症状', trigger: 'blur' }]">
          <el-input v-model="newClient.csymptom" placeholder="请输入症状" ></el-input>
        </el-form-item>
        <el-form-item label="时间" prop="cdate" :rules="[{ required: true, message: '请输入时间', trigger: 'blur' }]">
          <el-input v-model="newClient.cdate" placeholder="请输入时间" ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="cremark" :rules="[{ required: true, message: '请输入备注', trigger: 'blur' }]">
          <el-input v-model="newClient.cremark" placeholder="请输入备注" ></el-input>
        </el-form-item>
        <el-form-item label="已购药品" prop="mno" :rules="[{ required: true, message: '请输入已购药品编号', trigger: 'blur' }]">
          <el-input v-model="newClient.mno" placeholder="请输入已购药品编号" ></el-input>
        </el-form-item>
        <el-form-item label="经办人" prop="ano" :rules="[{ required: true, message: '请输入经办人编号', trigger: 'blur' }]">
          <el-input v-model="newClient.ano" placeholder="请输入经办人编号" ></el-input>
        </el-form-item>


      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAdd = false">取 消</el-button>
        <el-button type="primary" @click="addClientConfirm">确 定</el-button>
      </span>

    </el-dialog>

    <!-- Update Client Dialog -->
    <el-dialog
        title="修改顾客信息"
        :visible.sync="dialogVisibleUpdate"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="newClient" ref="form" label-width="100px">
        <el-form-item label="编号" prop="cno" >
          <el-input v-model="newClient.cno" placeholder="请输入编号" ></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="cname" >
          <el-input v-model="newClient.cname" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="csex" >
          <el-select v-model="newClient.csex" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="cage" >
          <el-input v-model="newClient.cage" placeholder="请输入年龄"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="cphone" >
          <el-input v-model="newClient.cphone" placeholder="请输入电话" ></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="caddress" >
          <el-input v-model="newClient.caddress" placeholder="请输入地址" ></el-input>
        </el-form-item>
        <el-form-item label="症状" prop="csymptom" >
          <el-input v-model="newClient.csymptom" placeholder="请输入症状" ></el-input>
        </el-form-item>
        <el-form-item label="时间" prop="cdate" >
          <el-input v-model="newClient.cdate" placeholder="请输入时间" ></el-input>
        </el-form-item>
        <el-form-item label="备注" prop="cremark" >
          <el-input v-model="newClient.cremark" placeholder="请输入备注" ></el-input>
        </el-form-item>
        <el-form-item label="已购药品" prop="mno" >
          <el-input v-model="newClient.mno" placeholder="请输入已购药品编号" ></el-input>
        </el-form-item>
        <el-form-item label="经办人" prop="ano" >
          <el-input v-model="newClient.ano" placeholder="请输入经办人编号" ></el-input>
        </el-form-item>


      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdate = false">取 消</el-button>
        <el-button type="primary" @click="UpdateClient">确 定</el-button>
      </span>

    </el-dialog>

  </el-container>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      clients: [],
      loading: false,
      noResults: false,
      searchParams: {
        cno: '',
        cname: '',
        csex: '',
        cage: '',
        cphone: '',
        caddress:'',
        csymptom:'',
        cdate:'',
        cremark:'',
        mno:'',
        ano:''
      },
      isAdmin: false,  // 用来判断当前用户是否有管理员权限
      dialogVisibleAdd: false,  // 控制添加药品对话框的显示与隐藏
      dialogVisibleUpdate:false,//控制修改药品对话框的显示与隐藏
      SelectionClients:[],   //已选中的药品
      newClient: {        // 添加药品表单的数据模型
        cno: '',
        cname: '',
        csex: '',
        cage: '',
        cphone: '',
        caddress:'',
        csymptom:'',
        cdate:'',
        cremark:'',
        mno:'',
        ano:''
      },
    };
  },
  methods: {
    // 查询所有药品
    findAll() {
      request.get('/client/findAll')
          .then(response => {
            console.log('response.data.data:',response.data.data);
            if(response.data.code === "200"){
              this.$message.success("获取药品数据成功");
              console.log('获取药品数据:', response.msg);
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

      request.get('/client/search',{params:arr})
          .then(response=>{
            const {code,msg}=response.data;
            if(code==='500'){
              this.$message.warning(msg);
              return;
            }

            console.log('response.data.data:', response.data.data);
            this.clients=response.data.data;
            console.log('搜索结果:', this.clients)
            this.noResults=this.clients.length===0;
            this.$message.success('搜索成功');
            this.loading=false;
          }).catch(error=>{
        this.loading=false;
        this.$message.error('搜索失败');
        this.clients=[];
        this.noResults=true;
      })
    },
    //重置搜索
    resetSearch(){
      this.searchParams = {
        cno: '',
        cname: '',
        csex: '',
        cage: '',
        cphone: '',
        caddress:'',
        csymptom:'',
        cdate:'',
        cremark:'',
        mno:'',
        ano:''
      };
      this.findAll();
      this.noResults=false;
    },


    // 编辑药品
    UpdateClient() {
      this.$refs.form.validate(valid => {
        if (valid) {
          request.put(`/client/update/${this.newClient.cno}`, this.newClient)
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
    deleteClient(client) {
      this.$confirm('确认删除该药品?', '删除提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete(`/client/delete/${client.cno}`)
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
      this.SelectionClients=selection;
    },
    //批量删除药品
    DeleteBatchClient() {
      if(this.SelectionClients.length===0){
        this.$message.warning("请选择要删除的药品");
        return;
      }
      this.$confirm(`确认要删除选中的${this.SelectionClients.length}个药品？`,'删除提示',{
        type:'warning'
      }).then(()=>{
        const clientCnos=this.SelectionClients.map(item=>item.cno);//获取选中药品的编号
        this.$request.delete('/client/deleteBatch',{data:clientCnos})//发送批量删除请求
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
    updateClient(client){
      this.newClient={...client}
      this.dialogVisibleUpdate=true;
    },
    // 添加药品确认
    addClientConfirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          request.post('/client/add', this.newClient)
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
      console.log("重置前:", this.newClient);
      //this.$refs.form.resetFields();
      this.newClient = {cno: '',
        cname: '',
        csex: '',
        cage: '',
        cphone: '',
        caddress:'',
        csymptom:'',
        cdate:'',
        cremark:'',
        mno:'',
        ano:''};
      console.log("重置后:", this.newClient);
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
