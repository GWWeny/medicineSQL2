<template>
  <div class="login-container">
    <div class="login-form">
      <div class="login-image">
        <img src="../assets/112.jpg" alt="Login Image" />
      </div>
      <div class="login-form-container">
        <el-form :model="user" :rules="rules" ref="loginForm" class="login-form">
          <div class="login-title">登录</div>
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="请输入用户名" v-model="user.username" size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" v-model="user.password" size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <div class="code-container">
              <el-input prefix-icon="el-icon-lock" placeholder="请输入验证码" v-model="user.code" size="medium"></el-input>
              <div class="code-image"></div>
              <!-- 使用 v-model 绑定 user.code -->
              <code-update ref="codeUpdate" v-model="user.code" :length="4" :width="120" :height="40" :fontSize="30"></code-update>
            </div>
          </el-form-item>
          <el-button type="primary" @click="login" class="login-button">登录</el-button>
          <div class="login-footer">
            <div class="forgot-password-link">忘记密码?请<span @click="() => { this.$router.push('/forgot-password') }">重置密码</span></div>
            <div class="register-link">注册账号?请<span @click="navigateToRegister">注册</span></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>


<script>
import CodeUpdate from "@/components/CodeUpdate.vue";
export default {
  name: "LoginForm3",
  components : {
    CodeUpdate
  },
  data() {
    //验证码的提示输入
    const codeBox=(rule,value,callback)=>{
      if(value===''){
        callback(new Error("请输入验证码"));
      }else if(value.toLowerCase()!==this.user.code.toLowerCase()){
        callback(new Error("验证码错误"));
      }else{
        callback();
      }
      };

    return {
      CodeUpdate:'',
      user: {
        username: '',
        password: '',
        code: ''
      },
      rules : {
        //点击方框之后的提示输入,必填，失去焦点后触发
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        code: [{ required: true, validator: codeBox, trigger: 'blur' }]
      }
    }
  },
  mounted() {
    //在组件挂在后初始验证码,二维码不显示的罪魁祸首,
    this.$nextTick(()=>{
      if(this.$refs.codeUpdate){
        this.$refs.codeUpdate.generateCode();//调用子组件的方法
      }
    })

  },
  methods: {
    login() {
      console.log("登录按钮被点击了");

      const generatedCode=this.$refs.codeUpdate.code;
      if (this.user.code.toLowerCase() !== generatedCode.toLowerCase()) {
        this.$message.error('验证码错误');
        return;  // 终止后续的登录操作
      }

      // 表单验证
      this.$refs["loginForm"].validate((valid) => {
        if(valid) {
          // 向后端发送登录请求
          this.$request.post("/login", this.user)
              .then(res => {
                // 后端返回成功的状态
                if(res.code === "200") {
                  this.$message.success("登录成功");


                  // 跳转到主页
                  this.$router.push("/home");
                  // 存储用户信息和Token到localStorage
                  console.log("localStorageUser:", localStorage.getItem("user"))
                  localStorage.setItem("localStorageUser", JSON.stringify(res.data));
                } else {
                  // 后端返回失败的状态
                  this.$message.error(res.msg);
                }
              }).catch(error => {

                // 捕获请求错误
                console.error("登录请求错误:", error);
                this.$message.error("登录请求失败，密码错误");
              });
        }
      });
    },
    navigateToRegister(){
      if(this.$router.currentRoute.path !=='/register'){
        this.$router.push('/register');
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
  padding: 30px;
  text-align: center;
}

.login-image {
  display: flex;
  justify-content: center; /* Center image horizontally */
  margin-bottom: 20px;    /* Space between image and form */
}

.login-image img {
  max-width: 100%;
  height: auto;           /* Maintain aspect ratio */
  border-radius: 8px;
}

.login-form-container {
  display: flex;
  flex-direction: column;  /* Stack elements vertically */
  align-items: center;      /* Center align items */
}

.login-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #333;
}

.code-container {
  display: flex;
  align-items: center;
  justify-content: center;
}

.code-image {
  margin-left: 10px;
}

.login-button {
  width: 100%;
  margin-top: 20px;
  font-size: 16px;
}

.login-footer {
  margin-top: 20px;
  font-size: 14px;
  color: #666;
}

.forgot-password-link,
.register-link {
  cursor: pointer;
  color: #409EFF;
}

.forgot-password-link span,
.register-link span {
  text-decoration: underline;
}

.forgot-password-link:hover,
.register-link:hover {
  color: #66b1ff;
}
</style>