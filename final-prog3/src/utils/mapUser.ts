import type { UsuarioDto } from "../types/api";
import type { IUser } from "../types/IUser";
import { Rol } from "../types/Rol";

export const mapUsuarioDto = (dto: UsuarioDto): IUser => ({
  id: dto.id,
  email: dto.mail,
  nombre: dto.nombre,
  apellido: dto.apellido,
  rol: dto.rol === "ADMIN" ? Rol.Admin : Rol.Client,
});
