<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        <img src="../assets/112.jpg" alt="logo">
      </div>
      <div class="login-form-container">
        <el-form :model="user" :rules="rules" ref="loginForm" class="login-form">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" placeholder="请输入用户名" v-model="user.username" size="medium"></el-input>
          </el-form-item>

          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" v-model="user.password" size="medium" type="password"></el-input>
          </el-form-item>

          <el-form-item prop="code">
            <div class="code-container">
              <el-input prefix-icon="el-icon-lock" placeholder="请输入验证码" v-model="user.code" size="medium"></el-input>
              <code-update ref="codeUpdate" :length="4" :width="120" :height="40" :fontSize="30" @update:value="getCode"></code-update>
            </div>
          </el-form-item>

          <el-button type="primary" @click="login" class="login-button">登录</el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import CodeUpdate from './CodeUpdate.vue';

export default {
  name: 'LoginForm',
  components: {
    CodeUpdate
  },
  data() {
    return {
      CodeUpdate:'',
      user: {
        username: '',
        password: '',
        code: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        code: [
          { required: true, validator: this.codeBox, trigger: 'blur' },
        ],
      },
    };
  },
  methods: {
    // 验证码输入验证
    codeBox(rule, value, callback) {
      if (value === "") {
        callback(new Error("请输入验证码"));
      } else if (value.toLowerCase() !== this.user.code.toLowerCase()) {
        callback(new Error("验证码错误"));
      } else {
        callback();
      }
    },
    // 获取验证码
    getCode(newCode) {
      this.code = newCode;  // 更新验证码
    },
    // 登录提交
    login() {
      console.log("被点击了");
      this.$refs["loginForm"].validate((valid) => {
        if (valid) {
          // 提交表单数据
          this.$request.post('/login', this.user)  // 调用后端的登录接口
              .then(res => {
                console.log("用户",this.user);
                if (res.code === 200) {
                  // 登录成功，跳转到主页
                  this.$message.success("登录成功");
                  this.$router.push('/home');

                  localStorage.setItem("localStorageUser", JSON.stringify(res.data))
                  console.log("localStorageUser:", localStorage.getItem("localStorageUser"))
                } else {
                  // 登录失败，显示错误信息
                  this.$message.error(res.data.msg);
                }
              })
              .catch(error => {
                consle.log("用户",this.user);
                this.$message.error("登录请求失败");
              });
        } else {
          console.log("表单验证失败");
        }
      });
    }
  },
  mounted(){
    this.$refs.codeUpdate.generateCode();
    // 刷新验证码
  }
};
</script>

<style scoped>
/* 设定背景和布局 */
.login-container {
  width: 100%;/*始终沾满*/
  height: 100%;/*始终沾满*/
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to right, #4facfe, #00f2fe); /* 渐变背景 */
}
/* 整体容器 */
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to right, #4facfe, #00f2fe); /* 渐变背景 */
}
/*第二层白色框框*/
.login-box{
  width: 350px;
  background: #fff;
  border-radius: 12px;/*给按钮的四个角添加圆角*/
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 30px;
}
/* 包含图片和表单的内容 */
.login-content {
  width: 400px;
  display: flex;
  flex-direction: column;
  justify-content: center; /* 垂直方向居中 */
  align-items: center; /* 水平方向居中 */
}

/* logo区域 */
.login-title {
  text-align:center;
  margin-bottom: 0px;
  flex-grow: 1;
}

.login-title img {
  width: 300px; /* logo宽度 */
  height: 300px; /* logo高度 */
  object-fit: cover;
  object-position: center center;
  /*object-fit: contain; /* 保证图片不变形 */
}
/* 将图片裁剪成圆形 */
/*.login-title img {
  border-radius: 50%;
}*/

/* 轻微的阴影效果 */
/*.login-title img {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}*/
.login-title img {
  animation: rotateLogo 3s infinite linear;
}
/*动画效果*/
/*@keyframes rotateLogo {
  0% {
    transform: rotate(0deg);
  }
  0% {
    transform: rotate(0deg);
  }
}*/
/*如果想让 logo 在页面加载时有一个渐显的效果，可以使用 opacity 和 transition
这个效果会让 logo 在鼠标悬停到 .login-container 时逐渐显示。
 */
/*.login-title img {
  opacity: 0;
  transition: opacity 1s ease-in-out;
}

.login-container:hover .login-title img {
  opacity: 1;
}*/

.login-title {
  margin-bottom: 40px; /* 增加间距 */
  margin-top: 20px; /* 可以给 logo 添加顶部间距 */
}

.login-form-container{
  padding:20px
}

/* 登录表单 */
.login-form {
  width: 80%;
  height: 80%;
  padding: 30px;
  background: #fff;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border-radius: 12px;
  margin-top: -60px; /* 通过负值来将表单上移 */
}

/* 输入框样式 */
.input-field {
  border-radius: 8px; /* 圆角输入框 */
  margin-bottom: 20px;
}

/* 验证码框容器 */
.code-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

/* 登录按钮样式 */
.login-button {
  width: 100%; /* 设置宽度为 100% 使按钮填满整个表单宽度 */
  max-width: 150px; /* 设置一个最大宽度来限制按钮的最大宽度 */
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

/* 表单项标题（可选） */
.el-form-item__label {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

/* 输入框和按钮的字体大小 */
.el-input__inner, .el-button {
  font-size: 14px;
}

/* 错误信息样式 */
.el-form-item__error {
  color: #ff4d4f;
  font-size: 12px;
}
</style>
