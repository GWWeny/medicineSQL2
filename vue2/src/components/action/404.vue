<template>
  <div class="page-404">
    <div class="error-content">
      <h1 class="error-code">404</h1>
      <p class="error-message">哎呀！页面不见了。</p >
      <p class="description">我们找不到你请求的页面。可能它已经被移除，或者你输入了错误的地址。</p >
      <router-link to="/home" class="home-link">返回首页</router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PageNotFound'
}
</script>

<style scoped>
.page-404 {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #FFB6C1, #FFD700); /* 渐变背景 */
  font-family: 'Arial', sans-serif;
}

.error-content {
  text-align: center;
  background: #fff;
  padding: 40px 60px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  max-width: 450px;
  width: 100%;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  color: #FF6347; /* 鲜艳的红色 */
  margin: 0;
  animation: bounce 1.5s ease infinite;
}

.error-message {
  font-size: 2em;
  color: #333;
  margin-top: 20px;
  font-weight: 600;
}

.description {
  font-size: 1.2em;
  color: #555;
  margin: 10px 0 30px;
}

.home-link {
  display: inline-block;
  padding: 12px 25px;
  font-size: 1.1em;
  color: #fff;
  background-color: #409EFF;
  text-decoration: none;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.home-link:hover {
  background-color: #66b1ff;
  transform: translateY(-2px);
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}
</style>