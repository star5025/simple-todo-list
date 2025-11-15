/**
 * 全局键盘事件处理器
 * 支持通过Enter键确认或关闭弹出消息
 */
class KeyboardHandler {
  constructor() {
    this.init()
  }

  init() {
    // 监听全局键盘事件
    document.addEventListener('keydown', this.handleGlobalKeydown.bind(this))
  }

  handleGlobalKeydown(event) {
    // 检查是否按下了Enter键且没有在输入框中
    if (event.key === 'Enter' && !this.isInputElement(event.target)) {
      // 检查是否有Element Plus的消息框打开
      const messageBox = document.querySelector('.el-message-box__wrapper')
      const message = document.querySelector('.el-message')
      
      // 如果有确认对话框打开，模拟点击确认按钮
      if (messageBox && !messageBox.classList.contains('el-message-box__wrapper--hidden')) {
        const confirmButton = messageBox.querySelector('.el-button--primary')
        if (confirmButton) {
          confirmButton.click()
          event.preventDefault()
          event.stopPropagation()
        }
        return
      }
      
      // 如果有消息提示打开，模拟点击关闭按钮
      if (message) {
        const closeBtn = message.querySelector('.el-message__closeBtn')
        if (closeBtn) {
          closeBtn.click()
          event.preventDefault()
          event.stopPropagation()
        }
      }
    }
    
    // 支持Escape键关闭消息框
    if (event.key === 'Escape' && !this.isInputElement(event.target)) {
      const messageBox = document.querySelector('.el-message-box__wrapper')
      if (messageBox && !messageBox.classList.contains('el-message-box__wrapper--hidden')) {
        const cancelButton = messageBox.querySelector('.el-button:not(.el-button--primary)')
        if (cancelButton) {
          cancelButton.click()
          event.preventDefault()
          event.stopPropagation()
        }
      }
    }
  }

  /**
   * 检查元素是否为输入框
   * @param {HTMLElement} element - 要检查的元素
   * @returns {boolean} 是否为输入框
   */
  isInputElement(element) {
    const inputTypes = ['INPUT', 'TEXTAREA']
    return inputTypes.includes(element.tagName) || 
           element.contentEditable === 'true' || 
           element.classList?.contains('el-textarea__inner') ||
           element.classList?.contains('el-input__inner') ||
           element.classList?.contains('el-button')
  }
}

// 创建并导出单例
export default new KeyboardHandler()