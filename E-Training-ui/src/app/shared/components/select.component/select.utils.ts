import {Option} from "./option";

export const optionsFromList = (list:any[], key:string, label:string): Option[] => {
    return list.map(item => <Option>{key: item[key], label: item[label]});
}
