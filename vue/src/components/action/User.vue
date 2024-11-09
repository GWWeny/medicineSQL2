<template>
  <el-card class="user-card">
    <div slot="header" class="card-header">
      <span>个人信息</span>
    </div>
    <el-form label-width="100px" :model="user" ref="userForm" class="form-container">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="user.username" disabled></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="user.phone" placeholder="请输入电话号码"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="user.email" placeholder="请输入邮箱地址"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="user.address" type="textarea" placeholder="请输入地址"></el-input>
      </el-form-item>
      <el-form-item label="身份" prop="role">
        <el-input v-model="user.role" disabled></el-input>
      </el-form-item>
    </el-form>
    <div class="form-footer">
      <el-button type="primary" @click="updateUser">保存</el-button>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "PersonalHomePage",
  data() {
    return {
      user: JSON.parse(localStorage.getItem("localStorageUser") || "{}")
    };
  },
  methods: {
    updateUser() {
      this.$request.put(`/user/update/${this.user.id}`, this.user).then(res => {
        if (res.code === "200") {
          this.$message.success("信息更新成功！");
        } else {
          this.$message.error(res.msg || "更新失败，请重试");
        }
        localStorage.setItem("localStorageUser", JSON.stringify(this.user));
        this.$emit("update:user", this.user);
      }).catch(() => {
        this.$message.error("请求失败，请稍后重试");
      });
    }
  }
};
</script>

<style scoped>
.user-card {
  max-width: 700px;
  margin: 20px auto;
  padding: 30px;
  background-color: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.card-header {
  font-size: 26px;
  font-weight: 600;
  color: #409eff;
  text-align: center;
  margin-bottom: 20px;
}

.form-container {
  margin-top: 20px;
}

.el-form-item__label {
  font-size: 16px;
  color: #555;
  font-weight: 500;
}

.el-input {
  border-radius: 8px;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.1);
}

.el-input--suffix {
  padding-right: 20px;
}

.el-button {
  width: 100%;
  border-radius: 24px;
  background-color: #409eff;
  border-color: #409eff;
  color: #fff;
  padding: 12px;
  font-size: 18px;
  transition: all 0.3s;
}

.el-button:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}

.form-footer {
  text-align: center;
  margin-top: 20px;
}

.el-input__inner {
  font-size: 16px;
}

.el-card {
  border: none;
}
</style>