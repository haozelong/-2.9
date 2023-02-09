/**
 * 用户角色
 */
import {StatusEnum} from './statusEnum';

export type UserRole = 0 | 1 | 2 | 3;

export const USER_ROLE = {
  student: {
    value: 0 as UserRole,
    description: '学生',
  } as StatusEnum<UserRole>,
  teacher: {
    value: 1 as UserRole,
    description: '老师',
  } as StatusEnum<UserRole>,
  manager: {
    value: 2 as UserRole,
    description: '管理员',
  } as StatusEnum<UserRole>,
  admin: {
    value: 3 as UserRole,
    description: '系统管理员',
  } as StatusEnum<UserRole>,
};
