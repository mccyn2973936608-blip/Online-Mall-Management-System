import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/views/Login.vue';
import Register from '@/views/Register.vue';
import Home from '@/views/Home.vue';
import Profile from '@/views/Profile.vue';
import MyOrders from '@/views/MyOrders.vue';
import ProductDetail from '@/views/ProductDetail.vue';
import Cart from '@/views/Cart.vue';
import Checkout from '@/views/Checkout.vue';
import AdminDashboard from '@/views/AdminDashboard.vue';
import AdminUsers from '@/views/AdminUsers.vue';
import AdminProducts from '@/views/AdminProducts.vue';
import AdminCategories from '@/views/AdminCategories.vue';
import AdminOrders from '@/views/AdminOrders.vue';
import { useUserStore } from '@/store/user';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/register', name: 'Register', component: Register },
  { path: '/profile', name: 'Profile', component: Profile, meta: { auth: true } },
  { path: '/orders', name: 'MyOrders', component: MyOrders, meta: { auth: true } },
  { path: '/product/:id', name: 'ProductDetail', component: ProductDetail },
  { path: '/cart', name: 'Cart', component: Cart, meta: { auth: true } },
  { path: '/checkout', name: 'Checkout', component: Checkout, meta: { auth: true } },
  { path: '/admin', name: 'AdminDashboard', component: AdminDashboard, meta: { auth: true, admin: true } },
  { path: '/admin/users', name: 'AdminUsers', component: AdminUsers, meta: { auth: true, admin: true } },
  { path: '/admin/products', name: 'AdminProducts', component: AdminProducts, meta: { auth: true, admin: true } },
  { path: '/admin/categories', name: 'AdminCategories', component: AdminCategories, meta: { auth: true, admin: true } },
  { path: '/admin/orders', name: 'AdminOrders', component: AdminOrders, meta: { auth: true, admin: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const userStore = useUserStore();
  if (to.meta.auth && !userStore.token) {
    next({ name: 'Login' });
    return;
  }
  if (to.meta.admin && userStore.role !== 1) {
    if (userStore.token) {
      next({ name: 'Home' });
    } else {
      next({ name: 'Login' });
    }
    return;
  }
  next();
});

export default router;
