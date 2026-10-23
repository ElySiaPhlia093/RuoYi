<template>
  <div class="login">
    <!-- 背景：整图完整展示（contain），四周用同图模糊放大补边 -->
    <div class="login-bg-blur"></div>
    <div class="login-bg-full">
      <img src="/login-bg.jpg" alt="" />
    </div>
    <div class="login-mask"></div>
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ title }}</h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          size="large"
          auto-complete="off"
          placeholder="账号"
        >
          <template #prefix><svg-icon icon-class="user" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin"
        >
          <template #prefix><svg-icon icon-class="password" class="el-input__icon input-icon" /></template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <template #prefix><svg-icon icon-class="validCode" class="el-input__icon input-icon" /></template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="large"
          type="primary"
          style="width:100%;"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ footerContent }}</span>
    </div>
  </div>
</template>

<script setup>
import { getCodeImg } from "@/api/login"
import Cookies from "js-cookie"
import { encrypt, decrypt } from "@/utils/jsencrypt"
import useUserStore from '@/store/modules/user'
import defaultSettings from '@/settings'

const title = import.meta.env.VITE_APP_TITLE
const footerContent = defaultSettings.footerContent
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const { proxy } = getCurrentInstance()

const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: ""
})

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }]
}

const codeUrl = ref("")
const loading = ref(false)
// 验证码开关
const captchaEnabled = ref(true)
// 注册开关
const register = ref(false)
const redirect = ref(undefined)

watch(route, (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect
}, { immediate: true })

function handleLogin() {
  proxy.$refs.loginRef.validate(valid => {
    if (valid) {
      loading.value = true
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 })
        Cookies.set("password", encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 })
      } else {
        // 否则移除
        Cookies.remove("username")
        Cookies.remove("password")
        Cookies.remove("rememberMe")
      }
      // 调用action的登录方法
      userStore.login(loginForm.value).then(() => {
        const query = route.query
        const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur]
          }
          return acc
        }, {})
        router.push({ path: redirect.value || "/", query: otherQueryParams })
      }).catch(() => {
        loading.value = false
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode()
        }
      })
    }
  })
}

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function getCookie() {
  const username = Cookies.get("username")
  const password = Cookies.get("password")
  const rememberMe = Cookies.get("rememberMe")
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  }
}

getCode()
getCookie()
</script>

<style lang='scss' scoped>
.login {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-color: #06101d;
  overflow: hidden;
}
/* 四周补边：同一张图模糊放大后压暗铺满 */
.login-bg-blur {
  position: absolute;
  inset: -60px;
  background: url('/login-bg.jpg') center center / cover no-repeat;
  filter: blur(60px) brightness(0.3) saturate(1.2);
  z-index: 0;
}
/* 完整背景图：contain 保证整张图都看得到，不变形也不裁切 */
.login-bg-full {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  img {
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
  }
}
/* 中间区域压暗，保证表单可读 */
.login-mask {
  position: absolute;
  inset: 0;
  z-index: 2;
  pointer-events: none;
  background:
    linear-gradient(180deg, rgba(3, 8, 16, 0.82) 0%, rgba(4, 10, 20, 0.0) 20%, rgba(4, 10, 20, 0.0) 80%, rgba(3, 8, 16, 0.86) 100%),
    radial-gradient(62% 72% at 50% 50%, rgba(4, 10, 20, 0.45) 0%, rgba(4, 10, 20, 0.2) 62%, rgba(4, 10, 20, 0.1) 100%);
}
.title {
  margin: 0 auto 26px auto;
  text-align: center;
  color: #f3f9ff;
  font-size: 20px;
  font-weight: 600;
  letter-spacing: 2px;
  text-shadow: 0 2px 12px rgba(0, 0, 0, 0.45);
}
.title::after {
  content: '工业互联网 · 设备运维一体化平台';
  display: block;
  margin-top: 8px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 1px;
  color: rgba(214, 236, 255, 0.7);
}
.login-form {
  position: relative;
  z-index: 3;
  width: 400px;
  padding: 30px 28px 14px 28px;
  border-radius: 14px;
  background: rgba(12, 22, 38, 0.45);
  border: 1px solid rgba(255, 255, 255, 0.18);
  box-shadow: 0 18px 50px rgba(0, 8, 20, 0.45);
  backdrop-filter: blur(18px);
  -webkit-backdrop-filter: blur(18px);

  :deep(.el-input__wrapper) {
    background: rgba(255, 255, 255, 0.1);
    box-shadow: 0 0 0 1px rgba(255, 255, 255, 0.22) inset;
  }
  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 0 1px rgba(64, 158, 255, 0.85) inset;
  }
  :deep(.el-input__inner) {
    color: #eef7ff;
    &::placeholder { color: rgba(214, 236, 255, 0.55); }
  }
  :deep(.el-input__inner:-webkit-autofill) {
    -webkit-text-fill-color: #eef7ff;
    transition: background-color 9999s ease-out 0s;
  }
  :deep(.el-input__icon) { color: rgba(214, 236, 255, 0.75); }
  :deep(.el-checkbox__label) { color: rgba(214, 236, 255, 0.85); }
  :deep(.el-checkbox__inner) {
    background: rgba(255, 255, 255, 0.12);
    border-color: rgba(255, 255, 255, 0.4);
  }
  :deep(.el-checkbox__input.is-checked .el-checkbox__inner),
  :deep(.el-checkbox__input.is-indeterminate .el-checkbox__inner) {
    background: #409eff;
    border-color: #409eff;
  }
  .el-input {
    height: 40px;
    input { height: 40px; }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 0px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: rgba(214, 236, 255, 0.6);
}
.login-code {
  width: 33%;
  height: 40px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: rgba(226, 240, 255, 0.72);
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 4;
  color: rgba(226, 240, 255, 0.78);
  text-shadow: 0 1px 6px rgba(0, 0, 0, 0.6);
}
.login-code-img {
  height: 40px;
  padding-left: 12px;
  border-radius: 4px;
  background: #fff;
}

html.dark .login {
  .login-form {
    background: rgba(10, 18, 32, 0.62) !important;
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.5);
  }
}
</style>
