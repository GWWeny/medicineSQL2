<template>
  <div class="code-update-container" style="width: 100%; height: 100%" @click="generateCode">
    <canvas ref="canvas" :width="width" :height="height"></canvas>
  </div>
</template>

<script>
export default {
  name: "CodeUpdate",
  props: {
    modelValue: {  // 使用 modelValue 作为 v-model 的绑定值
      type: String,
      default: ''
    },
    length: {
      type: Number,
      default: 4
    },
    width: {
      type: Number,
      default: 120
    },
    height: {
      type: Number,
      default: 30
    },
    fontSize: {
      type: Number,
      default: 30
    }
  },
  data() {
    return {
      code: this.modelValue // 将 modelValue 初始化为 code
    };
  },
  methods: {
    generateCode() {
      const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
      this.code = '';
      for (let i = 0; i < this.length; i++) {
        this.code += characters.charAt(Math.floor(Math.random() * characters.length));
      }
      this.drawCode();
      this.$emit('update:modelValue', this.code.toLowerCase()); // 使用 update:modelValue 事件
    },
    drawCode() {
      const canvas = this.$refs.canvas;
      const ctx = canvas.getContext('2d');
      ctx.clearRect(0, 0, this.width, this.height); // Clear previous drawings
      ctx.fillStyle = '#f5f5f5';
      ctx.fillRect(0, 0, this.width, this.height);
      ctx.font = `${this.fontSize}px Arial`;
      ctx.textAlign = 'center';
      ctx.textBaseline = 'middle';
      ctx.fillStyle = '#333';
      ctx.fillText(this.code, this.width / 2, this.height / 2);

      // Add some random lines for a better effect
      for (let i = 0; i < 5; i++) {
        ctx.beginPath();
        ctx.moveTo(Math.random() * this.width, Math.random() * this.height);
        ctx.lineTo(Math.random() * this.width, Math.random() * this.height);
        ctx.strokeStyle = '#ccc';
        ctx.stroke();
      }
    },
  },
  mounted() {
    this.generateCode(); // Generate code on mount
  }
};
</script>

<style scoped>
.code-update-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
