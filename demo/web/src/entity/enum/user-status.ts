/**
 * 用户状态
 */
import {StatusEnum} from './statusEnum';

export type UserStatus = 0 | 1;

/**
 * freezing是正在进行时，指正在冻结中。
 * 在当前项目中指：由正常变为冻结的瞬间。
 * 比如正在加载中，我们会使用单词loading
 * 而在这应该用过去分启frozen，表示已经被冻结的
 */
export const USER_STATUS: {[index: string]: StatusEnum<UserStatus>} = {
  frozen: {
    value: 0 as UserStatus,
    description: '冻结',
    clazz: 'secondary'
  } as StatusEnum<UserStatus>,
  normal: {
    value: 1 as UserStatus,
    description: '正常',
    clazz: 'success'
  } as StatusEnum<UserStatus>,
};
