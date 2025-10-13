// 辅助函数：根据名称获取 Cookie 的值
function getCookieValue(name) {
  if (!document.cookie) return null; // 如果没有 Cookie，直接返回 null
  const value = `; ${document.cookie}`; // 在开头加 '; ' 便于统一处理
  const parts = value.split(`; ${name}=`); // 按 '; cookieName=' 分割
  if (parts.length === 2) {
    return parts.pop().split(';').shift().trim();
  }
  return null;
}
//辅助函数：清除所有cookies
function clearCookies() {
    let cookies = document.cookie.split(";"); // 获取所有Cookie，并将其分割成数组
    cookies.forEach(cookie => {
        let cookieName = cookie.split("=")[0]; // 获取Cookie的名称
        document.cookie = cookieName + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT"; // 设置过期时间为1970年1月1日
    });
}

function addCookieValue(name, newValue) {
    // 读取现有的 cookie 值
    const existingValue = getCookieValue(name);
    
    // 如果存在现有值，则追加；否则直接设置新值
    const updatedValue = existingValue ? existingValue + ',' + newValue : newValue;
    
    // 设置更新后的 cookie
    document.cookie = `${name}=${updatedValue}; path=/`;
}

// 删除指定名称的Cookie
function deleteCookie(name) {
    // 设置Cookie的过期时间为过去的时间，使其立即失效
    document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/`;
}

//弹窗提示
function showToast(message) {
    // 创建弹窗容器
    const toast = document.createElement('div');
    
    // 设置弹窗样式
    toast.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: white;
        padding: 20px 30px;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
        z-index: 9999;
        font-size: 16px;
        text-align: center;
        min-width: 200px;
        max-width: 80%;
        word-wrap: break-word;
        opacity: 0;
        transition: opacity 0.3s ease;
    `;
    
    // 设置弹窗内容
    toast.textContent = message;
    
    // 将弹窗添加到页面
    document.body.appendChild(toast);
    
    // 触发淡入效果
    setTimeout(() => {
        toast.style.opacity = '1';
    }, 10);
    
    // 2秒后自动消失
    setTimeout(() => {
        toast.style.opacity = '0';
        // 动画结束后移除元素
        setTimeout(() => {
            if (toast.parentNode) {
                toast.parentNode.removeChild(toast);
            }
        }, 300);
    }, 2000);
}


function getUrlParam(url, paramName) {
    try {
        // 如果没有传入 url，则使用当前页面的 URL
        const urlObj = new URL(url || window.location.href);
        const params = new URLSearchParams(urlObj.search);
        return params.get(paramName);
    } catch (error) {
        console.error('URL 解析错误:', error);
        return null;
    }
}