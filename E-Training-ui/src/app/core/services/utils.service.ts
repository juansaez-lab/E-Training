import {HttpHeaders, HttpParams} from "@angular/common/http";
import ConstUrls from "../../shared/contants/const-urls";
import ConstLocalStorage from "../../shared/contants/const-local-storage";
import { User } from "src/app/interfaces/user";
import {UserResponseDTO} from "../../interfaces/UserResponseDTO";

export default async function to(promise: Promise<any>) {
    try {
        const data = await promise
        return data
    } catch (err) {
        return [err]
    }
}

export function isOkResponse(response) {
    if (response && response.status === 200) {
        return true
    }
    return false
}

export function loadResponseData(response) {
    return response.body;
}

export function loadResponseError(response) {
    if (!response || !response.body || !response.body.exception) {
        return "Error inesperado de servidor";
    } else {
        return response.body.exception.codigoDeError + ' ' + response.body.exception.mensajeDeError;
    }
}

export const headers = new HttpHeaders({
    'Content-Type': 'application/json'
});

export function loadCredentials(): HttpParams {
    const loggedUser: UserResponseDTO = getUser();
    return new HttpParams()
        .set(ConstUrls.NICK_USUARIO_PARAM, loggedUser.username)
}

export function storeUser(user: UserResponseDTO) {
    localStorage.setItem(ConstLocalStorage.USER_INFO, JSON.stringify(user));
}

export function getUser(): UserResponseDTO {
    return JSON.parse(localStorage.getItem(ConstLocalStorage.USER_INFO));
}
