require_relative 'CaseClassMixin'

module CaseObjectMixin

  include CaseClassMixin

  def self.attr_accessor(*args)
    throw('Un case object no puede definir atributos.')
  end

  def copy
    self
  end

end