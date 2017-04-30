require_relative '../lib/modules/SyntaxModule.rb'
require 'test/unit'

class CaseClassTest < Test::Unit::TestCase

  def setup

    case_class Alumno do
      attr_accessor :nombre, :nota
    end
    case_class Curso do
      attr_accessor :materia, :codigo, :alumnos
    end

    @alumno_fede = Alumno.new('Fede',6)
    @otro_alumno_igual = Alumno('Fede', 6)
    @otro_alumno_distinto = Alumno('Pedro', 2)

    @curso_tadp = Curso('TADP', 'K3031', [Alumno('Nico', 8),Alumno.new('Miguel', 2)])

  end

  def test_case_class_new
    assert_equal @alumno_fede.nombre, 'Fede'
    assert_equal @alumno_fede.nota, 6
    assert_equal @curso_tadp.codigo, 'K3031'
  end

  def test_case_class_inmutable
    assert_equal @alumno_fede.nombre, 'Fede'
    assert_raises(NoMethodError) do
      @alumno_fede.nota=8
    end
  end

  def test_case_class_defaults
    # Testing == method
    assert_equal @alumno_fede, @otro_alumno_igual
    assert_not_equal @alumno_fede, @otro_alumno_distinto
    # Testing hash method
    assert_equal @alumno_fede.hash, @otro_alumno_igual.hash
    assert_not_equal @alumno_fede.hash, @otro_alumno_distinto.hash
    # Testing to_s method
    assert_equal @alumno_fede.to_s, 'Alumno(Fede, 6)'
    assert_equal @curso_tadp.to_s, 'Curso(TADP, K3031, Alumno(Nico, 8), Alumno(Miguel, 2))'
  end

  #TODO: tests para la parte de herencia y para lo que resta del tp

end