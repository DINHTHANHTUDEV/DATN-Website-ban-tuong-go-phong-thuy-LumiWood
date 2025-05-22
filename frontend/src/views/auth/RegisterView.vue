

<template>
  <div class="register-view d-flex align-items-center justify-content-center min-vh-100 bg-light">
    <div class="card shadow-sm" style="width: 100%; max-width: 450px;">
      <div class="card-body p-4 p-lg-5">
        <h2 class="card-title text-center mb-4">Đăng Ký Tài Khoản</h2>
        <form @submit.prevent="handleRegister" novalidate>

          
          <div v-if="error" class="alert alert-danger" role="alert">
            {{ error }}
          </div>
          
          <div v-if="successMessage" class="alert alert-success" role="alert">
            {{ successMessage }} Đang chuyển hướng đến trang đăng nhập...
            {}
          </div>

          
          <div class="mb-3">
            <label for="username" class="form-label">Tên đăng nhập <span class="text-danger">*</span></label>
            <input
              type="text"
              class="form-control"
              :class="{ 'is-invalid': validationErrors.username }"
              id="username"
              v-model.trim="formData.username"
            required
            placeholder="Ít nhất 3 ký tự"
            :disabled="loading || !!successMessage"
            aria-describedby="usernameFeedback"
            />
            <div id="usernameFeedback" v-if="validationErrors.username" class="invalid-feedback">
              {{ validationErrors.username }}
            </div>
          </div>

          
          <div class="mb-3">
            <label for="fullName" class="form-label">Họ và Tên</label>
            <input
              type="text"
              class="form-control"
              :class="{ 'is-invalid': validationErrors.fullName }"
              id="fullName"
              v-model.trim="formData.fullName"
            placeholder="Ví dụ: Nguyễn Văn A"
            :disabled="loading || !!successMessage"
            aria-describedby="fullNameFeedback"
            />
            <div id="fullNameFeedback" v-if="validationErrors.fullName" class="invalid-feedback">
              {{ validationErrors.fullName }}
            </div>
          </div>

          
          

          
          <div class="mb-3">
            <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
            <input
              type="password"
              class="form-control"
              :class="{ 'is-invalid': validationErrors.password }"
              id="password"
              v-model="formData.password"
              required
              placeholder="Ít nhất 6 ký tự"
              :disabled="loading || !!successMessage"
              aria-describedby="passwordFeedback"
            />
            <div id="passwordFeedback" v-if="validationErrors.password" class="invalid-feedback">
              {{ validationErrors.password }}
            </div>
          </div>

          
          <div class="mb-3">
            <label for="confirmPassword" class="form-label">Xác nhận mật khẩu <span class="text-danger">*</span></label>
            <input
              type="password"
              class="form-control"
              :class="{ 'is-invalid': validationErrors.confirmPassword }"
              id="confirmPassword"
              v-model="formData.confirmPassword"
              required
              placeholder="Nhập lại mật khẩu"
              :disabled="loading || !!successMessage"
              aria-describedby="confirmPasswordFeedback"
            />
            <div id="confirmPasswordFeedback" v-if="validationErrors.confirmPassword" class="invalid-feedback">
              {{ validationErrors.confirmPassword }}
            </div>
          </div>

          
          <div class="d-grid">

            <button type="submit" class="btn btn-primary btn-lg" :disabled="loading || !!successMessage">
              <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
              {{ loading ? 'Đang xử lý...' : 'Đăng ký' }}
            </button>
          </div>
        </form>
        <div class="text-center mt-3 small">

          Bạn đã có tài khoản? <RouterLink :to="{name: 'login'}">Đăng nhập</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { RouterLink, useRouter } from 'vue-router';
import apiClient from '@/http/axios.js';


const formData = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  fullName: '',

});
const validationErrors = reactive({});
const loading = ref(false);
const error = ref(null);
const successMessage = ref(null);
const router = useRouter();


const validateForm = () => {

  Object.keys(validationErrors).forEach(key => delete validationErrors[key]);
  let isValid = true;


  if (!formData.username || formData.username.length < 3 || formData.username.length > 50) {
    validationErrors.username = 'Tên đăng nhập phải từ 3 đến 50 ký tự.';
    isValid = false;
  } else if (/\s/.test(formData.username)) {
    validationErrors.username = 'Tên đăng nhập không được chứa khoảng trắng.';
    isValid = false;
  }


  if (formData.fullName && formData.fullName.length > 150) {
    validationErrors.fullName = 'Họ tên không được quá 150 ký tự.';
    isValid = false;
  }


  if (!formData.password || formData.password.length < 6 || formData.password.length > 100) {
    validationErrors.password = 'Mật khẩu phải từ 6 đến 100 ký tự.';
    isValid = false;
  }


  if (!formData.confirmPassword) {
    validationErrors.confirmPassword = 'Vui lòng xác nhận mật khẩu.';
    isValid = false;
  } else if (formData.password && formData.password !== formData.confirmPassword) {
    validationErrors.confirmPassword = 'Mật khẩu xác nhận không khớp.';
    isValid = false;
  }


  

  return isValid;
};


const handleRegister = async () => {

  error.value = null;
  successMessage.value = null;


  if (!validateForm()) {
    console.log('DEBUG: Client-side validation failed.', validationErrors);
    return;
  }

  loading.value = true;

  try {

    const payload = {
      username: formData.username,
      password: formData.password,
      fullName: formData.fullName || null,

    };
    console.log('DEBUG: Sending registration payload:', payload);


    const response = await apiClient.post('/auth/register', payload);
    console.log('DEBUG: Registration API response:', response);


    successMessage.value = response.data.message || 'Đăng ký thành công!';


    Object.keys(formData).forEach(key => { formData[key] = ''; });

    Object.keys(validationErrors).forEach(key => delete validationErrors[key]);

    console.log('DEBUG: Register successful. Redirecting to login in 2 seconds...');


    setTimeout(() => {
      try {
        console.log('DEBUG: Executing redirect to login page');
        router.push({ name: 'login' });
      } catch(navError) {
        console.error('DEBUG: Failed to redirect after registration:', navError);

        error.value = "Đăng ký thành công nhưng lỗi chuyển trang. Vui lòng đăng nhập thủ công.";

      }
    }, 2000);

  } catch (err) {

    console.error("Registration API failed:", err);
    if (err.response) {
      console.error("Error Response Data:", err.response.data);
      console.error("Error Response Status:", err.response.status);

      if (err.response.data && (err.response.data.error || err.response.data.message)) {
        error.value = err.response.data.error || err.response.data.message;
      } else if (err.response.status === 400) {
        error.value = "Dữ liệu gửi đi không hợp lệ. Vui lòng kiểm tra lại các trường đã nhập.";
      } else if (err.response.status === 409) {
        error.value = "Tên đăng nhập hoặc email đã tồn tại.";
      }
      else {
        error.value = `Lỗi từ máy chủ (mã ${err.response.status}). Vui lòng thử lại.`;
      }
    } else if (err.request) {

      console.error("Network Error:", err.request);
      error.value = 'Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng và thử lại.';
    } else {

      console.error('Error', err.message);
      error.value = 'Đã có lỗi không xác định xảy ra. Vui lòng thử lại.';
    }
  } finally {


    if (error.value && !successMessage.value) {
      loading.value = false;
    }


  }
};
</script>

<style scoped>
.register-view {
  background: url('https://goldviet24k.vn/pic/Product/images/c%C3%B3c%20ng%E1%BA%ADm%20ti%E1%BB%81n%20v%C3%A0ng%20%C4%91%E1%BB%93ng%201d.jpg') no-repeat center center;
  background-size: cover;
}

.card {
  border: none; 
}

.invalid-feedback {
  display: block;
  min-height: 1.2em; 
  
}
.form-control.is-invalid ~ .invalid-feedback {
  
}
.text-danger {
  font-size: 0.9em; 
}

</style>

