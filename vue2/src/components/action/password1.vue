<template>
  <div class="change-password-container">
    <div class="change-password-box">
      <el-form :model="form" :rules="rules" ref="changePasswordForm" class="change-password-form">
        <div class="form-title">修改密码</div>


        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
              type="password"
              v-model="form.oldPassword"
              prefix-icon="el-icon-lock"
              placeholder="请输入旧密码"
              size="medium"
          ></el-input>
        </el-form-item>


        <el-form-item label="新密码" prop="newPassword">
          <el-input
              type="password"
              v-model="form.newPassword"
              prefix-icon="el-icon-lock"
              placeholder="请输入新密码"
              size="medium"
          ></el-input>
        </el-form-item>


        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
              type="password"
              v-model="form.confirmPassword"
              prefix-icon="el-icon-lock"
              placeholder="请确认新密码"
              size="medium"
          ></el-input>
        </el-form-item>


        <el-form-item>
          <el-button class="submit-button" type="primary" @click="submitForm" :disabled="loading">
            {{ loading ? '正在提交...' : '提交' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";  // 确保引入请求工具

export default {
  name: "ChangePassword",
  data() {
    return {
      form: {
        oldPassword: '',   // 旧密码
        newPassword: '',   // 新密码
        confirmPassword: '' // 确认密码
      },
      oldPassword: '',   // 从后端获取的旧密码
      loading: false,     // 控制按钮状态
      rules: {
        oldPassword: [
          {required: true, validator: this.validateOldPassword, trigger: 'blur'},
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
        ],
        confirmPassword: [
          {required: true, validator: this.validateConfirmPassword, trigger: 'blur'},
        ]
      }
    };
  },
  methods: {
    // 旧密码验证
    validateOldPassword(rule, value, callback) {
      if (value !== this.oldPassword) {
        callback(new Error('旧密码不正确'));
      } else {
        callback();
      }
    },

    // 新密码与确认密码验证
    validateConfirmPassword(rule, value, callback) {
      if (value !== this.form.newPassword) {
        callback(new Error('新密码与确认密码不一致'));
      } else {
        callback();
      }
    },

    // 提交表单
    submitForm() {
      console.log("提交表单", this.form);
      this.user.password = this.form.newPassword;
      delete this.user.email;
      console.log("修改密码", this.user);

      this.user = JSON.parse(localStorage.getItem("localStorageUser") || "{}");
      console.log("用户666", this.user);

      this.$refs.changePasswordForm.validate((valid) => {
        if (valid) {
          console.log("用户777", this.user.data);
          this.$request.put("/user/updateUserById", this.user.data).then(res => {
            console.log("修改密码2", res)
            if (res.data.code === "200") {
              this.$router.push("/login");
              this.$message.success("修改密码成功");
            } else {
              this.$message.error(res.msg);
            }
          })
        }
      });
    }
  },
  mounted() {
    // 获取用户信息，加载旧密码
    this.user = JSON.parse(localStorage.getItem("localStorageUser") || "{}");
    console.log("用户1", this.user);
    request.get(`/user/getUserById/${this.user.data.id}`).then(res => {
      console.log("用户2", res.data);
      this.token = res.data.data.token;
      console.log("token", this.token);
      this.oldPassword = res.data.data.password;
      console.log("密码", this.oldPassword);
    });
  }
}
</script>

<style scoped>
.change-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f6f8;
  background: linear-gradient(to right, #4facfe, #00f2fe); /* 渐变背景 */
}

.change-password-box {
  background-color: #fff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 450px;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  text-align: center;
  margin-bottom: 30px;
}

.submit-button {
  width: 100%;
  font-size: 16px;
  margin-top: 20px;
}

.el-form-item label {
  font-size: 14px;
}

.el-input {
  border-radius: 4px;
}

.el-button[disabled] {
  background-color: #ddd;
  cursor: not-allowed;
}
</style>