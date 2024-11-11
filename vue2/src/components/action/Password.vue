<!--<template>
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
          { required: true, message: '请输入旧密码', trigger: 'blur' },
          { validator: this.validateOldPassword, trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '新密码至少6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' },
          { validator: this.validateConfirmPassword, trigger: 'blur' }
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
    async submitForm() {
      this.$refs.changePasswordForm.validate(async (valid) => {
        if (valid) {
          try {
            this.loading = true;  // 提交过程中，显示加载中状态
            // 执行密码修改请求
            const response = await request.put("/user/updateUserById", {
              id: this.user.id,    // 假设已经在mounted里获取到了用户ID
              oldPassword: this.form.oldPassword,
              newPassword: this.form.newPassword
            });

            if (response.code === "200") {
              this.$message.success("密码修改成功");
              this.$router.push("/login");  // 跳转到登录页
            } else {
              this.$message.error(response.msg || '修改密码失败');
            }
          } catch (error) {
            this.$message.error('发生错误，请稍后重试');
          } finally {
            this.loading = false;  // 提交完后恢复按钮状态
          }
        }
      });
    }
  },
  mounted() {
    // 获取用户信息，加载旧密码
    this.user = JSON.parse(localStorage.getItem("localStorageUser") || "{}");
    request.get(`/user/getUserById/${this.user.id}`).then(res => {
      if (res.data) {
        this.oldPassword = res.data.password;
      }
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