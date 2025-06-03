import apiClient from "../../axios";

// Thống kê hôm nay
export const getTodayStats = async () => {
  const res = await apiClient.get("/api/statistics/today");
  return res.data;
};

// Thống kê 7 ngày gần đây
export const getLast7DaysStats = async () => {
  const res = await apiClient.get("/api/statistics/last-7-days");
  return res.data;
};

// Thống kê tháng hiện tại
export const getCurrentMonthStats = async () => {
  const res = await apiClient.get("/api/statistics/current-month");
  return res.data;
};

// Thống kê cơ bản (không cần tham số ngày)
export const getBasicStats = async () => {
  const res = await apiClient.get("/api/statistics/basic-stats");
  return res.data;
};

// Doanh thu biểu đồ line chart theo thời gian
export const getRevenueOverTime = async (startDateStr, endDateStr) => {
  if (!startDateStr || !endDateStr) {
    throw new Error("Start date and end date are required");
  }
  const res = await apiClient.get("/api/statistics/revenueovertime", {
    params: {
      start: startDateStr,
      end: endDateStr,
    },
  });
  return res.data;
};
