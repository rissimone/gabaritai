export type ConcursoStatus =
  'PREVISTO' | 'EDITAL_PUBLICADO' | 'INSCRICOES_ABERTAS' | 'PROVA_REALIZADA';

export type EducationLevel = 'FUNDAMENTAL' | 'MEDIO' | 'TECNICO' | 'SUPERIOR';

export interface ConcursoResponse {
  id: string;
  orgao: string;
  bancaOrganizadora: string;
  cargo: string;
  numeroEdital: string | null;
  dataProva: string | null;
  escolaridade: EducationLevel | null;
  disciplinas: string[];
  quantidadeVagas: number | null;
  linkOficial: string | null;
  status: ConcursoStatus;
  createdAt: string;
  updatedAt: string;
}

export interface CriarConcursoRequest {
  orgao: string;
  bancaOrganizadora: string;
  cargo: string;
  numeroEdital: string | null;
  dataProva: string | null;
  escolaridade: EducationLevel | null;
  disciplinas: string[];
  quantidadeVagas: number | null;
  linkOficial: string | null;
  status: ConcursoStatus | null;
}
