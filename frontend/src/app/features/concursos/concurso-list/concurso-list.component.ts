import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ConcursoService } from '../../../core/services/concurso.service';
import { extractErrorMessage } from '../../../core/utils/api-error.util';
import { ConcursoResponse, ConcursoStatus } from '../../../models/concurso.models';

const STATUS_OPTIONS: { value: ConcursoStatus; label: string }[] = [
  { value: 'PREVISTO', label: 'Previsto' },
  { value: 'EDITAL_PUBLICADO', label: 'Edital publicado' },
  { value: 'INSCRICOES_ABERTAS', label: 'Inscricoes abertas' },
  { value: 'PROVA_REALIZADA', label: 'Prova realizada' },
];

const STATUS_LABELS: Record<ConcursoStatus, string> = Object.fromEntries(
  STATUS_OPTIONS.map((option) => [option.value, option.label]),
) as Record<ConcursoStatus, string>;

@Component({
  selector: 'app-concurso-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './concurso-list.component.html',
  styleUrl: './concurso-list.component.scss',
})
export class ConcursoListComponent implements OnInit {
  private readonly concursoService = inject(ConcursoService);

  readonly statusOptions = STATUS_OPTIONS;
  readonly concursos = signal<ConcursoResponse[]>([]);
  readonly loading = signal(true);
  readonly errorMessage = signal<string | null>(null);
  readonly statusFilter = signal<ConcursoStatus | ''>('');

  ngOnInit(): void {
    this.load();
  }

  onFilterChange(value: string): void {
    this.statusFilter.set(value as ConcursoStatus | '');
    this.load();
  }

  statusLabel(status: ConcursoStatus): string {
    return STATUS_LABELS[status];
  }

  private load(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    const filtro = this.statusFilter() || undefined;

    this.concursoService.listConcursos(filtro).subscribe({
      next: (concursos) => {
        this.concursos.set(concursos);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set(extractErrorMessage(error));
        this.loading.set(false);
      },
    });
  }
}
