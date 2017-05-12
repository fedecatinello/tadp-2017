require_relative 'CaseClassMixin'

module CaseObjectMixin

  include CaseClassMixin

  def initialize(*args)
    @case_object_name = args.first
    self.freeze
  end

  def copy
    self
  end

  def to_s
    @case_object_name.to_s
  end

end

module CaseObjectClassMixin

  def attr_accessor(*args)
    throw 'No se pueden definir atributos en un Case Object.'
  end

end