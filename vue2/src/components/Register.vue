<template>
  <div class="register-container">
    <div class="register-box">
      <div class="register-form-container">
        <el-form ref="registerForm" :model="user" :rules="rules" label-width="100px">
          <div class="register-title">注册</div>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="user.username" prefix-icon="el-icon-former" size="medium"></el-input>
          </el-form-item>

          <el-form-item label="密码"  prop="请输入密码">
            <el-input prefix-icon="el-icon-lock" show-password prop="" v-model="user.password"></el-input>
          </el-form-item>

          <el-form-item label="确认密码" show-password prop="confirmPassword">
            <el-input prefix-icon="el-icon-lock" show-password prop="" v-model="user.confirmPassword"></el-input>
          </el-form-item>

          <el-form-item prop="role">
            <el-select v-model="user.role" placeholder="您的身份">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button class="register-button" type="primary" @click="register">注册</el-button>
          </el-form-item>

          <div class="footer-links">
            <div class="login-link">已有帐户? 请 <span @click="() => { this.$router.push('/login') }">登录</span></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Register",
  data() {
    return {
      options:[
        {value:'admin',label:'admin'},
        {value:'user',label:'user'},
      ],
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role:'',
      },
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, validator:this.validateOldPassword, trigger: 'blur'},
        ],
        confirmPassword: [
          {required: true, validator: this.validateConfirmPassword, trigger: 'blur'},
        ],
        role: [
          {required: true, message: '请选择您的身份', trigger: 'change'},
        ],
      },
    };
  },
  methods: {
    validateConfirmPassword(rule, value, callback) {
      if(value === ''){
        callback(new Error('请输入密码'))
      }else if(value !== this.user.password){
        callback(new Error('密码不一致'));
      }else{
        callback();
      }
    },
    validateOldPassword(rule, value, callback) {
      if(value === ''){
        callback(new Error('请输入密码'))
      }else{
        callback();
      }
    },
    register() {
      console.log("被点击了");
      console.log("用户数据:", this.user);
      this.$refs["registerForm"].validate((valid) => {
        console.log("表单验证结果:", valid);
        if (valid) {
          // 将注册信息发送到后端
          this.$request.post("/register", this.user).then(res => {
            console.log("服务器响应:", res);
            if (res.data.code === '200') {
              this.$message.success("注册成功");
              this.$router.push("/login"); // 注册成功后跳转到登录页面
            } else {
              this.$message.error(res.msg);
            }
          }).catch(() => {
            this.$message.error("注册请求失败，请稍后重试");
          });
        }
      });
    },
  }
}
</script>

<style>
.register-container {
  height: 100vh;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
}

.register-box {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  display: flex;
  width: 80%;
  max-width: 800px;
}

.register-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.register-form-container {
  flex: 1.5;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.register-title {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 20px;
}

.register-button {
  width: 100%;
}

.footer-links {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.login-link {
  color: #409EFF;
}

.login-link span {
  cursor: pointer;
}

.forgot-password-link a {
  color: #409EFF;
}
</style>