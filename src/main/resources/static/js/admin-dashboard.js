const BASE_URL = '/fd-simulator';
let productModal, productUsersModal;

document.addEventListener('DOMContentLoaded', function() {
    productModal = new bootstrap.Modal(document.getElementById('productModal'));
    productUsersModal = new bootstrap.Modal(document.getElementById('productUsersModal'));
    
    loadDashboardStats();
    loadProducts();
    loadCustomers();
    loadDeposits();
    loadAuditLogs();
});

function showSection(sectionName) {
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    
    document.getElementById(sectionName + '-section').classList.add('active');
    
    document.querySelectorAll('.sidebar .nav-link').forEach(link => {
        link.classList.remove('active');
    });
    event.target.closest('.nav-link').classList.add('active');
}

async function loadDashboardStats() {
    try {
        const response = await apiFetch('/api/admin/dashboard/stats');
        const stats = await response.json();
        
        document.getElementById('totalProducts').textContent = stats.totalFixedDeposits || 0;
        document.getElementById('totalCustomers').textContent = stats.totalCustomers || 0;
        document.getElementById('totalDeposits').textContent = stats.totalFixedDeposits || 0;
        document.getElementById('activeDeposits').textContent = stats.activeFixedDeposits || 0;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

async function loadProducts() {
    try {
        const response = await apiFetch('/api/admin/products');
        const products = await response.json();
        
        const tbody = document.getElementById('productsTableBody');
        tbody.innerHTML = '';
        
        if (products.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No products found</td></tr>';
            return;
        }
        
        products.forEach(product => {
            const row = `
                <tr>
                    <td>${product.id}</td>
                    <td><strong>${product.productName}</strong></td>
                    <td>${product.interestRate}%</td>
                    <td>₹${product.minAmount.toLocaleString()}</td>
                    <td>₹${product.maxAmount.toLocaleString()}</td>
                    <td>${product.minTenureMonths} - ${product.maxTenureMonths}</td>
                    <td>
                        <span class="badge ${product.isActive ? 'bg-success' : 'bg-secondary'}">
                            ${product.isActive ? 'Active' : 'Inactive'}
                        </span>
                    </td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="editProduct(${product.id})">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="viewProductUsers(${product.id})">
                            <i class="fas fa-users"></i>
                        </button>
                        <button class="btn btn-sm btn-danger" onclick="deleteProduct(${product.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading products:', error);
    }
}

async function loadCustomers() {
    try {
        const response = await apiFetch('/api/admin/users/customers');
        const customers = await response.json();
        
        const tbody = document.getElementById('customersTableBody');
        tbody.innerHTML = '';
        
        if (customers.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" class="text-center">No customers found</td></tr>';
            return;
        }
        
        customers.forEach(customer => {
            const row = `
                <tr>
                    <td>${customer.id}</td>
                    <td>${customer.username}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phoneNumber || 'N/A'}</td>
                    <td>${customer.preferredCurrency}</td>
                    <td>${customer.preferredLanguage}</td>
                    <td>${new Date(customer.createdAt).toLocaleDateString()}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="viewCustomerDetails(${customer.id})">
                            <i class="fas fa-eye"></i> View
                        </button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading customers:', error);
    }
}

async function loadDeposits() {
    try {
        const response = await apiFetch('/api/admin/fixed-deposits');
        const deposits = await response.json();
        
        const tbody = document.getElementById('depositsTableBody');
        tbody.innerHTML = '';
        
        if (deposits.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" class="text-center">No deposits found</td></tr>';
            return;
        }
        
        deposits.forEach(deposit => {
            const statusClass = {
                'ACTIVE': 'bg-success',
                'MATURED': 'bg-info',
                'CLOSED': 'bg-secondary',
                'PENDING': 'bg-warning'
            }[deposit.status] || 'bg-secondary';
            
            const row = `
                <tr>
                    <td>${deposit.id}</td>
                    <td>${deposit.user.username}</td>
                    <td>${deposit.fdProduct.productName}</td>
                    <td>₹${deposit.principalAmount.toLocaleString()}</td>
                    <td>${deposit.interestRate}%</td>
                    <td>${deposit.tenureMonths} months</td>
                    <td><span class="badge ${statusClass}">${deposit.status}</span></td>
                    <td>${new Date(deposit.startDate).toLocaleDateString()}</td>
                    <td>
                        <button class="btn btn-sm btn-primary" onclick="viewDepositDetails(${deposit.id})">
                            <i class="fas fa-eye"></i>
                        </button>
                    </td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading deposits:', error);
    }
}

async function loadAuditLogs() {
    try {
        const response = await apiFetch('/api/admin/audit-logs?page=0&size=50');
        const data = await response.json();
        const logs = data.content || [];
        
        const tbody = document.getElementById('auditTableBody');
        tbody.innerHTML = '';
        
        if (logs.length === 0) {
            tbody.innerHTML = '<tr><td colspan="7" class="text-center">No audit logs found</td></tr>';
            return;
        }
        
        logs.forEach(log => {
            const row = `
                <tr>
                    <td>${log.id}</td>
                    <td>${log.user.username}</td>
                    <td><span class="badge bg-primary">${log.action}</span></td>
                    <td>${log.entityType || 'N/A'}</td>
                    <td>${log.entityId || 'N/A'}</td>
                    <td>${log.ipAddress}</td>
                    <td>${new Date(log.timestamp).toLocaleString()}</td>
                </tr>
            `;
            tbody.innerHTML += row;
        });
    } catch (error) {
        console.error('Error loading audit logs:', error);
    }
}

function showCreateProductModal() {
    document.getElementById('productModalTitle').innerHTML = '<i class="fas fa-plus"></i> Create New Product';
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';
    document.getElementById('isActive').checked = true;
    productModal.show();
}

async function editProduct(id) {
    try {
        const response = await apiFetch(`/api/admin/products/${id}`);
        const product = await response.json();
        
        document.getElementById('productModalTitle').innerHTML = '<i class="fas fa-edit"></i> Edit Product';
        document.getElementById('productId').value = product.id;
        document.getElementById('productName').value = product.productName;
        document.getElementById('interestRate').value = product.interestRate;
        document.getElementById('minAmount').value = product.minAmount;
        document.getElementById('maxAmount').value = product.maxAmount;
        document.getElementById('minTenure').value = product.minTenureMonths;
        document.getElementById('maxTenure').value = product.maxTenureMonths;
        document.getElementById('description').value = product.description || '';
        document.getElementById('isActive').checked = product.isActive;
        
        productModal.show();
    } catch (error) {
        console.error('Error loading product:', error);
        alert('Error loading product details');
    }
}

async function saveProduct() {
    const productId = document.getElementById('productId').value;
    const productData = {
        productName: document.getElementById('productName').value,
        interestRate: parseFloat(document.getElementById('interestRate').value),
        minAmount: parseFloat(document.getElementById('minAmount').value),
        maxAmount: parseFloat(document.getElementById('maxAmount').value),
        minTenureMonths: parseInt(document.getElementById('minTenure').value),
        maxTenureMonths: parseInt(document.getElementById('maxTenure').value),
        description: document.getElementById('description').value,
        isActive: document.getElementById('isActive').checked
    };
    
    try {
        let response;
        if (productId) {
            response = await apiFetch(`/api/admin/products/${productId}`, {
                method: 'PUT',
                body: JSON.stringify(productData)
            });
        } else {
            response = await apiFetch('/api/admin/products', {
                method: 'POST',
                body: JSON.stringify(productData)
            });
        }
        
        if (response.ok) {
            alert('Product saved successfully!');
            productModal.hide();
            loadProducts();
            loadDashboardStats();
        } else {
            alert('Error saving product');
        }
    } catch (error) {
        console.error('Error saving product:', error);
        alert('Error saving product');
    }
}

async function deleteProduct(id) {
    if (!confirm('Are you sure you want to delete this product?')) {
        return;
    }
    
    try {
        const response = await apiFetch(`/api/admin/products/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            alert('Product deleted successfully!');
            loadProducts();
            loadDashboardStats();
        } else {
            alert('Error deleting product');
        }
    } catch (error) {
        console.error('Error deleting product:', error);
        alert('Error deleting product');
    }
}

async function viewProductUsers(productId) {
    try {
        const response = await apiFetch(`/api/admin/products/${productId}/users`);
        const users = await response.json();
        
        const tbody = document.getElementById('productUsersTableBody');
        tbody.innerHTML = '';
        
        if (users.length === 0) {
            tbody.innerHTML = '<tr><td colspan="4" class="text-center">No customers using this product</td></tr>';
        } else {
            users.forEach(user => {
                const row = `
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>${user.phoneNumber || 'N/A'}</td>
                        <td>${new Date(user.createdAt).toLocaleDateString()}</td>
                    </tr>
                `;
                tbody.innerHTML += row;
            });
        }
        
        productUsersModal.show();
    } catch (error) {
        console.error('Error loading product users:', error);
        alert('Error loading product users');
    }
}

function viewCustomerDetails(customerId) {
    alert('Customer details view - Coming soon! Customer ID: ' + customerId);
}

function viewDepositDetails(depositId) {
    alert('Deposit details view - Coming soon! Deposit ID: ' + depositId);
}

function filterCustomers() {
    const searchTerm = document.getElementById('customerSearch').value.toLowerCase();
    const rows = document.querySelectorAll('#customersTableBody tr');
    
    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchTerm) ? '' : 'none';
    });
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('username');
    window.location.href = BASE_URL + '/login';
}
