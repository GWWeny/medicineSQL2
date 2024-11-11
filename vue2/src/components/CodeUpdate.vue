<template>
  <div class="code-update" @click="generateCode">
    <canvas ref="canvas" :width="width" :height="height"></canvas>
  </div>
</template>

<script>
export default {
  props: {
    length: {
      type: Number,
      default: 4, // 默认验证码长度为4
    },
    width: {
      type: Number,
      default: 120, // 默认宽度
    },
    height: {
      type: Number,
      default: 40, // 默认高度
    },
    fontSize: {
      type: Number,
      default: 30, // 默认字体大小
    },
  },
  data() {
    return {
      code: '',
    };
  },
  methods: {
    // 生成验证码
    generateCode() {
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext('2d');
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      let code = '';
      // 生成随机验证码
      for (let i = 0; i < this.length; i++) {
        code += chars.charAt(Math.floor(Math.random() * chars.length));
      }

      // 清空之前的内容
      ctx.clearRect(0, 0, this.width, this.height);

      // 设置背景色
      ctx.fillStyle = '#f0f0f0';
      ctx.fillRect(0, 0, this.width, this.height);

      // 绘制验证码文本
      ctx.font = `${this.fontSize}px Arial`;
      ctx.fillStyle = '#333';
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillText(code, this.width / 2, this.height / 2);

      // 保存验证码到组件的数据
      this.code = code;

      // 触发外部的更新事件
      this.$emit('update:value', this.code);
    },
  },
  mounted() {
    this.generateCode();
  },
};
</script>

<style scoped>
.code-update {
  cursor: pointer;
  display: inline-block;
  position: relative;
}
canvas {
  display: block;
  border: 1px solid #ddd;
  border-radius: 5px;
}
</style>
