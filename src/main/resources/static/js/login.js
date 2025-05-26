document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            // JWT login isteği
            fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Login failed');
                }
                return response.json();
            })
            .then(data => {
                // JWT token'ı localStorage'a kaydet
                localStorage.setItem('token', data.token);
                // Ana sayfaya yönlendir
                window.location.href = '/dashboard';
            })
            .catch(error => {
                console.error('Login error:', error);
                // Hata mesajını göster
                const alertDiv = document.createElement('div');
                alertDiv.className = 'alert alert-danger';
                alertDiv.innerHTML = '<i class="fas fa-exclamation-circle"></i> Giriş başarısız. Lütfen bilgilerinizi kontrol edin.';
                loginForm.insertBefore(alertDiv, loginForm.firstChild);
                
                // 5 saniye sonra hata mesajını kaldır
                setTimeout(() => {
                    alertDiv.remove();
                }, 5000);
            });
        });
    }
}); 