<template>
  <el-container>
    <!-- 顶部导航栏 -->
    <el-header style="background-color: #26a69a; color: white; padding: 0 20px;">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <!-- 系统Logo和名称 -->
        <div style="font-size: 22px; font-weight: bold;" @click = "SystemHomePage">医药管理系统</div>
        <!-- 顶部导航 -->
        <el-menu mode="horizontal" background-color=#26a69a text-color="white" active-text-color="#1affff">
          <el-menu-item index="/home" @click="SystemHomePage">系统首页</el-menu-item>
          <el-menu-item index="/medicine" @click="MedicineManagement">药品管理</el-menu-item>
          <el-menu-item index="/agency" @click="AgencyManagement">经办管理</el-menu-item>
          <el-menu-item index="/client" @click="ClientManagement">顾客管理</el-menu-item>
        </el-menu>

        <el-dropdown trigger="click">
          <el-button style="color: black;">{{ localStorageUser.data.username }}</el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item index="/login" @click.native="PersonalHomePage">个人信息</el-dropdown-item>
            <el-dropdown-item index="/login" @click.native="ChangePassword">修改密码</el-dropdown-item>
            <el-dropdown-item index="/login" @click.native="GotoExit">退出系统</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>

    <el-main>
      <router-view @update:user="updateUser"></router-view>
    </el-main>
  </el-container>
</template>

<script>
import router from "@/router";

export default {
  name: 'HomeView',
  data() {
    return {
      localStorageUser: JSON.parse(localStorage.getItem("localStorageUser") || "{}"),
      activeMenu: this.$route.path // 设置当前激活的菜单项
    }
  },
  watch: {
    '$route.path': function(newPath) {
      this.activeMenu = newPath; // 路由变化时更新活动菜单
    }
  },
  methods: {
    GotoExit() {
      this.$confirm('是否退出系统?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem("localStorageUser");
        this.$router.push("/login");
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出'
        });
      });
    },
    PersonalHomePage() {
      router.push("/user");
    },
    updateUser() {
      this.localStorageUser = JSON.parse(localStorage.getItem("localStorageUser") || "{}");
    },
    ChangePassword() {
      router.push("/password");
    },
    SystemHomePage() {
      router.push("/home");
    },
    MedicineManagement() {
      router.push("/medicine");
    },
    AgencyManagement() {
      router.push("/agency");
    },
    ClientManagement() {
      router.push("/client");
    }
  }
}
</script>

<style scoped>
/* 顶部导航栏样式 */
.el-header {
  background-color: #001529;
  color: white;
}

.el-menu-item {
  font-size: 16px;
}

.el-menu-item:hover {
  background-color: #1890ff;
}

.el-dropdown-menu {
  background-color: #001529;
  color: white;
}
</style>
