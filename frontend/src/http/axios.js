//  gọi API backend ở đây
const mockApiClient = () => Promise.resolve({ data: {} });
const apiClient = {
  get: mockApiClient,
  post: mockApiClient,
  put: mockApiClient,
  delete: mockApiClient,
  patch: mockApiClient,
  defaults: { headers: { common: {} } }
};
export default apiClient;
