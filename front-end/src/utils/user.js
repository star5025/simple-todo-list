import request from './request'
import { ElMessage } from 'element-plus'

/**
 * 用户信息服务
 */

/**
 * 获取当前用户信息
 * @returns {Promise<Object|null>} 用户信息对象或null
 */
export async function getCurrentUserInfo() {
  try {
    // 从localStorage中获取用户信息
    const userId = localStorage.getItem('userId')
    const userName = localStorage.getItem('userName')
    
    if (userId && userName) {
      return {
        userId: parseInt(userId), // 确保userId是数字类型
        userName: userName
      }
    } else {
      return null
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    return null
  }
}

/**
 * 清除用户信息缓存
 */
export function clearUserInfoCache() {
  // 无需操作，因为现在不使用缓存
}

/**
 * 从JWT token中解析用户ID（如果后端JWT token包含用户信息）
 * @param {string} token JWT token
 * @returns {number|null} 用户ID或null
 */
export function parseUserIdFromToken(token) {
  if (!token) return null
  
  try {
    // 解析JWT token
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    
    const payload = JSON.parse(jsonPayload)
    const userId = payload.userId || null
    // 确保返回的是数字类型
    return userId ? parseInt(userId) : null
  } catch (error) {
    console.error('解析JWT token失败:', error)
    return null
  }
}