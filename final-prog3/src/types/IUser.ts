import type { Rol } from "./Rol";

export interface IUser {
  id: number;
  email: string;
  nombre: string;
  apellido: string;
  rol: Rol;
}
