//  gọi API backend ở đây
// const mockApiClient = () => Promise.resolve({ data: {} });
// const apiClient = {
//   get: mockApiClient,
//   post: mockApiClient,
//   put: mockApiClient,
//   delete: mockApiClient,
//   patch: mockApiClient,
//   defaults: { headers: { common: {} } }
// };
// export default apiClient;
import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080", // chỉnh đúng base URL của bạn
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
});

export default instance;
