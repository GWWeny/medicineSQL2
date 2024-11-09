// src/eventBus.js
import Vue from 'vue';
export const EventBus = new Vue();
//任何组件都可以通过EventBus.$emit()来发布事件
