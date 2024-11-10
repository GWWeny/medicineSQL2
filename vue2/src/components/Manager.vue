<template>
  <el-container>
    <!-- 左侧导航栏 -->
    <el-aside width="250px" class="sidebar">
      <el-menu :default-active="activeMenu" class="el-menu-vertical-demo" @select="handleMenuSelect">
        <el-menu-item index="/home" @click="SystemHomePage">系统首页</el-menu-item>
        <el-menu-item index="/medicine" @click="MedicineManagement">药品管理</el-menu-item>
        <el-menu-item index="/agency" @click="TransactorManagement">经办人管理</el-menu-item>
        <el-menu-item index="/client" @click="CustomerManagement">顾客管理</el-menu-item>
        <el-menu-item index="/person" @click="PersonalHomePage">个人信息</el-menu-item>
        <el-menu-item index="/password" @click="ChangePassword">修改密码</el-menu-item>
        <el-menu-item index="exit" @click="GotoExit">退出系统</el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区域 -->
    <el-container>
      <el-header class="header">
        <div class="header-title">
          <span>欢迎使用医药管理系统</span>
          <el-button @click="GotoExit" type="danger" class="exit-btn" icon="el-icon-power-off">退出</el-button>
        </div>
      </el-header>

      <el-main>
        <router-view></router-view> <!-- 动态加载页面 -->
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import router from "@/router";

export default {
  name: 'ManagerView',
  data() {
    return {
      user: JSON.parse(localStorage.getItem("localStorageUser") || '{}'),
      activeMenu: this.$route.path, // 当前路由路径，菜单高亮
    };
  },
  watch: {
    '$route.path': function (newPath) {
      this.activeMenu = newPath;
    },
  },
  methods: {
    handleMenuSelect(index) {
      this.activeMenu = index;
    },
    GotoExit() {
      this.$confirm('是否退出系统？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        localStorage.removeItem("localStorageUser");
        this.$router.push('/login');
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出',
        });
      });
    },
    PersonalHomePage() {
      this.$router.push('/person');
    },
    ChangePassword() {
      this.$router.push('/password');
    },
    SystemHomePage() {
      this.$router.push('/home');
    },
    MedicineManagement() {
      this.$router.push('/medicine');
    },
    TransactorManagement() {
      this.$router.push('/agency');
    },
    CustomerManagement() {
      this.$router.push('/client');
    },
  },
};
</script>

<style scoped>
/* 主容器 */
.el-container {
  height: 100vh;
}

/* 左侧导航栏 */
.sidebar {
  background-color: #34495e;
  color: #fff;
}

.el-menu-vertical-demo {
  width: 250px;
  background-color: #34495e;
  border-right: none;
}

.el-menu-item {
  font-size: 16px;
  color: #ecf0f1;
}

.el-menu-item.is-active {
  background-color: #1abc9c;
  color: #fff;
}

/* 顶部header */
.header {
  background-color: #2c3e50;
  color: #fff;
  padding: 10px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  font-size: 20px;
  font-weight: bold;
}

.exit-btn {
  background-color: #e74c3c;
  color: #fff;
}

/* 右侧主内容区域 */
.el-main {
  padding: 20px;
  background-color: #ecf0f1;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

/* 菜单项及按钮 */
.el-button {
  margin-top: 20px;
}

/* 页面小组件样式 */
.el-header, .el-footer, .el-aside, .el-main {
  background-color: #ecf0f1;
}
</style>