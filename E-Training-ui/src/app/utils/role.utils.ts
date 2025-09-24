import Role from "../roles";
import {Option} from "../shared/components/select.component/option";

export const userRoles = () => {
    return Object.values(Role).filter(r => r !== Role.SUPER).map(r => <Option>{key: r, label: r});
}
