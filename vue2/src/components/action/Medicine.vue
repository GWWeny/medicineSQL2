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
        <el-form-item label="药品编号" prop="mno">
          <el-input  v-model="searchParams.mno" placeholder="按药品编号搜索"></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="mname">
          <el-input v-model="searchParams.mname" placeholder="按药品名称搜索"></el-input>
        </el-form-item>
        <el-form-item label="服用方法" prop="mmode">
          <el-select v-model="searchParams.mmode" placeholder="按服用方法搜索">
            <el-option label="内服" value="内服"></el-option>
            <el-option label="外用" value="外用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="药效" prop="mefficacy">
          <el-input v-model="searchParams.mefficacy" placeholder="按药效搜索"></el-input>
        </el-form-item>
          <el-button @click="Search" type="primary" icon="el-icon-search">搜索</el-button>
          <el-button @click="resetSearch" type="warning" icon="el-icon-refresh">重置</el-button>
      </el-form>

      <el-table :data="medicines" style="width: 100%; margin-top: 20px;" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="mno" label="药品编号" width="250"></el-table-column>
        <el-table-column prop="mname" label="药品名称" width="250"></el-table-column>
        <el-table-column prop="mmode" label="服用方法" width="250"></el-table-column>
        <el-table-column prop="mefficacy" label="药效" width="250"></el-table-column>
        <el-table-column label="操作" width="200">
          <template v-slot="scope">
            <el-button v-if="isAdmin" @click="updateMedicine(scope.row)" type="primary" icon="el-icon-edit">编辑</el-button>
            <el-button v-if="isAdmin" @click="deleteMedicine(scope.row)" type="danger" icon="el-icon-delete">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-button v-if="isAdmin" @click="openAddDialog" type="success" icon="el-icon-plus" style="margin-top: 20px;">添加药品</el-button>
      <el-button v-if="isAdmin" @click="DeleteBatchMedicine" style="margin-top: 40px;" >批量删除</el-button>
    </el-main>

    <!-- Add Medicine Dialog -->
    <el-dialog
        title="添加药品信息"
        :visible.sync="dialogVisibleAdd"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="newMedicine" ref="form" label-width="100px">
        <el-form-item label="药品编号" prop="mno" :rules="[{ required: true, message: '请输入编号', trigger: 'blur' }]">
          <el-input v-model="newMedicine.mno" placeholder="请输入药品编号" ></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="mname" :rules="[{ required: true, message: '请输入药品名称', trigger: 'blur' }]">
          <el-input v-model="newMedicine.mname" placeholder="请输入药品名称"></el-input>
        </el-form-item>
        <el-form-item label="服用方法" prop="mmode" :rules="[{ required: true, message: '请输入服用方法', trigger: 'blur' }]">
          <el-select v-model="newMedicine.mmode" required placeholder="请选择服用方法">
            <el-option label="内服" value="内服"></el-option>
            <el-option label="外用" value="外用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="药品药效" prop="mefficacy" :rules="[{ required: true, message: '请输入药效', trigger: 'blur' }]">
          <el-input v-model="newMedicine.mefficacy" placeholder="请输入药效"></el-input>
        </el-form-item>


      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAdd = false">取 消</el-button>
        <el-button type="primary" @click="addMedicineConfirm">确 定</el-button>
      </span>

    </el-dialog>

    <!-- Update Medicine Dialog -->
    <el-dialog
        title="修改药品信息"
        :visible.sync="dialogVisibleUpdate"
        width="500px"
        @close="resetForm"
    >
      <el-form :model="newMedicine" ref="form" label-width="100px">
        <el-form-item label="药品编号" prop="mno" >
          <el-input v-model="newMedicine.mno" disabled placeholder="请输入药品编号" ></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="mname" >
          <el-input v-model="newMedicine.mname" placeholder="请输入药品名称"></el-input>
        </el-form-item>
        <el-form-item label="服用方法" prop="mmode" >
          <el-select v-model="newMedicine.mmode" placeholder="请选择服用方法">
            <el-option label="内服" value="内服"></el-option>
            <el-option label="外用" value="外用"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="药品药效" prop="mefficacy" >
          <el-input v-model="newMedicine.mefficacy" placeholder="请输入药效"></el-input>
        </el-form-item>


      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdate = false">取 消</el-button>
        <el-button type="primary" @click="UpdateMedicine">确 定</el-button>
      </span>

    </el-dialog>

  </el-container>
</template>

<script>
import request from "@/utils/request";
export default {
  data() {
    return {
      medicines: [],
      loading: false,
      noResults: false,
      searchParams: {
        mno: '',
        mname: '',
        mmode: '',
        mefficacy: ''
      },
      isAdmin: false,  // 用来判断当前用户是否有管理员权限
      dialogVisibleAdd: false,  // 控制添加药品对话框的显示与隐藏
      dialogVisibleUpdate:false,//控制修改药品对话框的显示与隐藏
      SelectionMedicines:[],   //已选中的药品
      newMedicine: {        // 添加药品表单的数据模型
        mno: '',
        mname: '',
        mmode: '',
        mefficacy: ''
      },
    };
  },
  methods: {
    // 查询所有药品
    findAll() {
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

      request.get('/medicine/search',{params:arr})
          .then(response=>{
            const {code,msg}=response.data;
            if(code==='500'){
              this.$message.warning(msg);
              return;
            }

            console.log('response.data.data:', response.data.data);
            this.medicines=response.data.data;
            console.log('搜索结果:', this.medicines)
            this.noResults=this.medicines.length===0;
            this.$message.success('搜索成功');
            this.loading=false;
          }).catch(error=>{
            this.loading=false;
            this.$message.error('搜索失败');
            this.medicines=[];
            this.noResults=true;
      })
    },
    //重置搜索
    resetSearch(){
      this.searchParams = {
        mno: '',
        mname: '',
        mmode: '',
        mefficacy: ''
      };
      this.findAll();
      this.noResults=false;
    },


    // 编辑药品
    UpdateMedicine() {
      this.$refs.form.validate(valid => {
        if (valid) {
            request.put(`/medicine/update/${this.newMedicine.mno}`, this.newMedicine)
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
    deleteMedicine(medicine) {
      this.$confirm('确认删除该药品?', '删除提示', {
        type: 'warning'
      }).then(() => {
        this.$request.delete(`/medicine/delete/${medicine.mno}`)
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
      this.SelectionMedicines=selection;
    },
    //批量删除药品
    DeleteBatchMedicine() {
      if(this.SelectionMedicines.length===0){
        this.$message.warning("请选择要删除的药品");
        return;
      }
      this.$confirm(`确认要删除选中的${this.SelectionMedicines.length}个药品？`,'删除提示',{
        type:'warning'
      }).then(()=>{
        const medicineMnos=this.SelectionMedicines.map(item=>item.mno);//获取选中药品的编号
        this.$request.delete('/medicine/deleteBatch',{data:medicineMnos})//发送批量删除请求
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
    updateMedicine(medicine){
      this.newMedicine={...medicine}
      this.dialogVisibleUpdate=true;
    },
    // 添加药品确认
    addMedicineConfirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          request.post('/medicine/add', this.newMedicine)
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
      console.log("重置前:", this.newMedicine);
      //this.$refs.form.resetFields();
      this.newMedicine = {mno: '', mname: '', mmode: '', mefficacy: ''};
      console.log("重置后:", this.newMedicine);
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
