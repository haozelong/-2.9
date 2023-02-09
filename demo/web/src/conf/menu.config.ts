import {BaseMenu} from '../common/base-menu';
import {USER_ROLE} from '../entity/enum/user-role';

/**
 * 菜单配置
 */
export const menus: Array<BaseMenu> = [
  {
    name: '首页',
    url: 'dashboard',
    icon: 'fas fa-tachometer-alt',
    defaultShow: true,

  },
  {
    name: '个人中心',
    url: 'personal',
    icon: 'fas fa-user',
  }
];
