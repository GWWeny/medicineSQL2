<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-image">
        <img src="@/assets/112.jpg" alt="Login Image">
      </div>
      <div class="login-form-container">
        <el-form :model="user" :rules="rules" ref="loginForm" class="login-form">
          <div class="login-title">登录</div>
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="请输入用户名" v-model="user.username" size="medium"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" show-password placeholder="请输入密码" v-model="user.password"></el-input>
          </el-form-item>
          <el-form-item prop="code">
            <div class="code-container">
              <el-input prefix-icon="el-icon-lock" placeholder="请输入验证码" v-model="user.code"></el-input>
              <div class="code-image">
                <code-update ref="codeUpdate" :length="4" :width="120" :height="40" :fontSize="30" @update:value="getCode"></code-update>
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button class="login-button" type="primary" @click="login">登录</el-button>
          </el-form-item>
          <div class="footer-links">
            <el-button @click="navigateToRegister" type="text">还没有注册? 请注册</el-button>
            <el-button @click="navigateToForgotPassword" type="text">忘记密码?</el-button>
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
  components: {
    CodeUpdate,
  },
  data() {
    return {
      CodeUpdate: '',
      user: {
        username: '',
        password: '',
        code: '',
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        code: [
          { required: true, message:'请输入验证码', trigger: 'blur' },
        ]
      }
    }
  },

  methods: {
    login() {
      console.log("被点击了");
      console.log("用户数据:", this.user);
      this.$refs["loginForm"].validate((valid) => {
        console.log("表单验证结果:", valid);
        console.log("this.CodeUpdate.toLowerCase()", this.CodeUpdate.toLowerCase());
        console.log("this.user.code.toLowerCase()", this.user.code.toLowerCase());
        //判断验证码是否正确
        if(this.CodeUpdate.toLowerCase() !== this.user.code.toLowerCase()){
          this.$message.error("验证码错误，请重新输入");
          return;
        }
        if (valid) {
          this.$request.post("/login", this.user).then(res => {

            console.log("服务器响应:", res);  // 打印返回的服务器响应
            if (res.data.code === "200") {
              this.$message.success("登录成功");
              localStorage.setItem("localStorageUser", JSON.stringify(res.data));
              console.log("localStorageUser:", localStorage.getItem("localStorageUser"));
              this.$router.push("/");
            }
          }).catch(error=>{
            this.$message.error("密码或用户名错误，请修改");
          });
        }
      });
    },
    getCode(code) {
      this.CodeUpdate = code;
    },
    navigateToForgotPassword() {
      this.$router.push('/forgot-password'); // 跳转到忘记密码页面
    },
    navigateToRegister() {
      this.$router.push("/register");  // 跳转到注册页面
    }
  }
}
</script>

<style>
.login-container {
  width: 100%;/*始终沾满*/
  height: 100%;/*始终沾满*/
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to right, #4facfe, #00f2fe); /* 渐变背景 */
}

.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to right, #4facfe, #00f2fe); /* 渐变背景 */
}

.login-box {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  width: 80%;
  max-width: 800px;
}

.login-image {
  flex: 1;
  border-top-left-radius: 8px;
  border-bottom-left-radius: 8px;
  overflow: hidden;
}

.login-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.login-form-container {
  flex: 1.5;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 20px;
}

/* 验证码框容器 */
.code-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.code-image {
  margin-left: 10px;
}

/* 登录按钮样式 */
.login-button {
  width: 100%; /* 设置宽度为 100% 使按钮填满整个表单宽度 */
  max-width: 300px; /* 设置一个最大宽度来限制按钮的最大宽度 */
  border-radius: 8px;/*给按钮的四个角添加圆角*/
  padding: 15px;/*内边距，按钮内部文本与按钮边缘的距离*/
  font-size: 16px;/*按钮文本的字体大小*/
  transition: background-color 0.3s ease;/*为background-color提供过渡动画*/
  margin: 15px auto; /* 通过 margin 自动居中按钮 */
  display: block; /* 使按钮成为块级元素，从而可以使用 auto margin 进行居中 */
}

/* 登录按钮悬停效果 */
.login-button:hover {
  background-color: #26a69a;
}

.footer-links {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.register-link span {
  cursor: pointer;
}

.forgot-password-link a {
  color: #409EFF;
}
</style>
