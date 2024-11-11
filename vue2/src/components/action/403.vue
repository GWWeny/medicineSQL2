<template>
  <div class="forbidden-page">
    <div class="message-container">
      <h1 class="error-code">403</h1>
      <p class="error-message">抱歉，您没有权限访问这个界面。</p >
      <el-button type="primary" class="go-back-button" @click="goBack">返回首页</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "ForbiddenPage",
  methods: {
    goBack() {
      // 返回首页或指定路由
      this.$router.push('/');
    }
  }
}
</script>

<style scoped>
.forbidden-page {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #FF6B6B, #FFD93D); /* 渐变背景 */
  font-family: 'Arial', sans-serif;
}

.message-container {
  text-align: center;
  padding: 40px 60px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 450px;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #f56c6c;
  margin: 0;
  animation: bounce 1.5s ease infinite;
}

.error-message {
  font-size: 20px;
  color: #333;
  margin: 20px 0;
}

.el-button {
  padding: 10px 30px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 25px;
  background-color: #409EFF;
  border: none;
  transition: all 0.3s;
}

.el-button:hover {
  background-color: #66b1ff;
  transform: translateY(-2px);
}

.go-back-button {
  background-color: #ff7f50;
  border-radius: 25px;
  font-weight: 600;
  transition: background-color 0.3s, transform 0.3s;
}

.go-back-button:hover {
  background-color: #ff8c69;
  transform: translateY(-2px);
}

/* 增加一个bounce动画效果 */
@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}
</style>